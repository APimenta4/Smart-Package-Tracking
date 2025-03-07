<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from "../store/auth-store";
import { useToast } from 'primevue/usetoast';

const route = useRoute();
const router = useRouter();
const sensorCode = route.params.code;

const config = useRuntimeConfig();
const api = config.public.API_URL;

const readings = ref([]);
const filters = ref({
  global: { value: null, matchMode: 'contains' }
});

const sensorTypeBadge = {
  LOCATION: 'info',
  TEMPERATURE: 'success', 
  ACCELERATION: 'warning'
};

const authStore = useAuthStore();
const toast = useToast();

if (!authStore.isAuthenticated || (authStore.user.role !== "Client" && authStore.user.role !== "Manager")) {
  router.push("/");
}

async function fetchReadings() {
  try {
    const response = await fetch(`${api}/sensors/${sensorCode}/readings`, {
      headers: {
        'Authorization': `Bearer ${authStore.token}`
      }
    });
    if (!response.ok) {
      const errorText = await response.text();
      let errorMessage = "Failed to fetch readings";
      try {
        const errorData = JSON.parse(errorText);
        errorMessage = errorData.message || errorMessage;
      } catch (e) {
        errorMessage = errorText;
      }
      throw new Error(errorMessage);
    }
    const data = await response.json();
    if (Array.isArray(data.readings)) {
      readings.value = data.readings.map(reading => ({
        ...reading,
        volumeCode: data.sensor.volumeCode,
        type: data.sensor.type,
        timestamp: new Date(reading.timestamp).toLocaleString()
      }));
    } else {
      console.error('Unexpected API response format:', data);
    }
  } catch (error) {
    console.error('Error fetching readings:', error);
    toast.add({ severity: 'error', summary: 'Error', detail: error.message, life: 3000 });
  }
}

onMounted(() => {
  if (authStore.isAuthenticated) {
    fetchReadings();
  }
});
</script>

<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow mb-12">
    <Card class="mt-10">
      <template #title>
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-bold">Readings for Sensor {{ sensorCode }}</h2>

        </div>
      </template>
      <template #content>
        <DataTable
          :value="readings"
          paginator
          :rows="10"
          :filters="filters"
          filterDisplay="menu"
          :rowsPerPageOptions="[5, 10, 20]"
          tableStyle="min-width: 50rem"
        >
          <template #header>
            <div class="flex justify-between">
              <span class="p-input-icon-left">
                <i class="pi pi-search mr-3" />
                <InputText 
                  v-model="filters['global'].value"
                  placeholder="Search readings..." 
                />
              </span>
            </div>
          </template>

          <Column field="volumeCode" header="Volume" sortable />
          <Column field="type" header="Type" sortable>
            <template #body="{ data }">
              <Tag
                :value="data.type"
                :severity="sensorTypeBadge[data.type]"
              />
            </template>
          </Column>
          <Column field="timestamp" header="Date" sortable />
          <Column header="Value" :exportable="false">
            <template #body="{ data }">
              <div>
                <template v-if="data.type === 'LOCATION'">
                  {{ data.latitude }}, {{ data.longitude }}
                </template>
                <template v-else-if="data.type === 'TEMPERATURE'">
                  {{ data.temperature }}°C
                </template>
                <template v-else-if="data.type === 'ACCELERATION'">
                  {{ data.acceleration }} G
                </template>
              </div>
            </template>
          </Column>
        </DataTable>
      </template>
    </Card>
  </div>
</template>