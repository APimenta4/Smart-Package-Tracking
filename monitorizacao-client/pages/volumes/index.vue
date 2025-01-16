<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '../store/auth-store';
import { useToast } from 'primevue/usetoast'; 
import { useRouter } from 'vue-router';

const config = useRuntimeConfig();
const api = config.public.API_URL;

const authStore = useAuthStore();
const toast = useToast(); 
const router = useRouter();

if (!authStore.isAuthenticated || (auth.user.role !== "Client" && auth.user.role !== "Manager")) {
  router.push("/");
}

const volumes = ref([]);
const filters = ref({
  global: { value: null, matchMode: 'contains' }
});

const statusSeverity = {
  READY_FOR_PICKUP: 'info',
  IN_TRANSIT: 'info',
  DELIVERED: 'success',
  RETURNED: 'warning',
  CANCELLED: 'danger'
};

const getPackageTypeSeverity = (type) => {
  const severityMap = {
    FRAGILE: "danger",
    ISOTERMIC: "warning",
    GEOLOCATION: "info",
    NONE: "secondary",
  };
  return severityMap[type] || "secondary";
};

const getStatusSeverity = (status) => {
  return statusSeverity[status] || "secondary";
};

async function fetchVolumes() {
  try {
    const response = await fetch(`${api}/volumes`, {
      headers: {
        'Authorization': `Bearer ${auth.token}` 
      }
    });
    if (!response.ok) {
      const errorText = await response.text();
      let errorMessage = "Failed to fetch volumes";
      try {
        const errorData = JSON.parse(errorText);
        errorMessage = errorData.message || errorMessage;
      } catch (e) {
        errorMessage = errorText;
      }
      throw new Error(errorMessage);
    }
    const data = await response.json();
    volumes.value = data;
  } catch (error) {
    console.error("Failed to fetch volumes:", error);
    toast.add({ severity: 'error', summary: 'Error', detail: error.message, life: 3000 }); 
  }
}

onMounted(() => {
  if (authStore.isAuthenticated) {
    fetchVolumes();
  }
});
</script>

<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow mb-12">
    <Card class="mt-10">
      <template #title>
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-bold">All Volumes</h2>
        </div>
      </template>
      <template #content>
        <DataTable
          :value="volumes"
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
                  placeholder="Search volumes..." 
                />
              </span>
            </div>
          </template>

          <Column field="code" header="Volume Code" sortable />
          <Column field="orderCode" header="Delivery" sortable />
          <Column field="packageType" header="Package Type" sortable>
            <template #body="{ data }">
              <div class="flex flex-wrap gap-1">
                <template v-for="type in data.packageType.split('_')" :key="type">
                  <Tag :value="type" :severity="getPackageTypeSeverity(type)" />
                </template>
              </div>
            </template>
          </Column>
          <Column field="status" header="Status" sortable>
            <template #body="{ data }">
              <Tag
                :value="data.status.replace(/_/g, ' ')" 
                :severity="getStatusSeverity(data.status)"
              />
            </template>
          </Column>
          <Column header="Actions" :exportable="false">
            <template #body="slotProps">
              <div class="flex gap-2">
                <Button
                  icon="pi pi-eye"
                  rounded
                  text
                  severity="info"
                  @click="navigateTo(`/volumes/${slotProps.data.code}`)"
                />
                <Button
                  icon="pi pi-chart-line"
                  rounded
                  text
                  severity="success"
                  @click="navigateTo(`/volumes/${slotProps.data.code}/readings`)"
                />
              </div>
            </template>
          </Column>
        </DataTable>
      </template>
    </Card>
  </div>
</template>