<script setup>
import { ref, onMounted } from "vue";
import { useAuthStore } from '../store/auth-store';
import { useToast } from 'primevue/usetoast';

const config = useRuntimeConfig();
const api = config.public.API_URL;

const router = useRouter();
const authStore = useAuthStore();
const toast = useToast(); 

if (!authStore.isAuthenticated || (auth.user.role !== "Client" && auth.user.role !== "Manager")) {
  router.push("/");
}

const volume = ref(null);

const statusSeverity = {
  READY_FOR_PICKUP: "info",
  IN_TRANSIT: "info",
  DELIVERED: "success",
  RETURNED: "warning",
  CANCELLED: "danger",
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

const getSensorTypeSeverity = (type) => {
  const severityMap = {
    TEMPERATURE: "warning",
    LOCATION: "info",
    ACCELERATION: "danger",
    NONE: "secondary",
  };
  return severityMap[type] || "secondary";
};

async function fetchVolume() {
  try {
    const response = await fetch(`${api}/volumes/${route.params.code}`, {
      headers: {
        'Authorization': `Bearer ${auth.token}` 
      }
    });
    if (!response.ok) {
      const errorText = await response.text();
      let errorMessage = "Failed to fetch volume";
      try {
        const errorData = JSON.parse(errorText);
        errorMessage = errorData.message || errorMessage;
      } catch (e) {
        errorMessage = errorText;
      }
      throw new Error(errorMessage);
    }
    const data = await response.json();
    volume.value = data;
  } catch (error) {
    console.error("Failed to fetch volume:", error);
    toast.add({ severity: 'error', summary: 'Error', detail: error.message, life: 3000 }); 
  }
}

onMounted(() => {
  if (authStore.isAuthenticated) {
    fetchVolume();
  }
});
</script>

<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow mb-12">
    <Card class="mt-10">
      <template #title>
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-bold">Volume Details</h2>
          <Button
            icon="pi pi-arrow-left"
            label="Back to Volumes"
            text
            @click="navigateTo('/volumes')"
          />
        </div>
      </template>
      <template #content>
        <div v-if="volume" class="flex flex-col gap-6">
          <!-- Basic Volume Info -->
          <div class="grid grid-cols-2 gap-6">
            <div class="flex flex-col gap-2">
                <div>
                <span class="text-sm font-medium">Volume Code</span>
                <div class="text-xl">{{ volume.code }}</div>
                </div>
                <div>
                <span class="text-sm font-medium">Delivery Code</span>
                <div class="text-xl">
                  <Button
                  link
                  class="text-blue-600 hover:text-blue-800 p-0 text-xl"
                  @click="navigateTo(`/deliveries/${volume.orderCode}`)"
                  >
                  {{ volume.orderCode }}
                  </Button>
                </div>
              </div>
            </div>

            <div class="flex flex-col gap-2">
              <div>
                <span class="text-sm font-medium">Status</span>
                <div>
                  <Tag
                    :value="volume.status.replace(/_/g, ' ')"
                    :severity="statusSeverity[volume.status]"
                    class="text-lg"
                  />
                </div>
              </div>
              <div>
                <span class="text-sm font-medium">Package Type</span>
                <div class="flex flex-wrap gap-1">
                  <template
                    v-for="type in volume.packageType.split('_')"
                    :key="type"
                  >
                    <Tag
                      :value="type"
                      :severity="getPackageTypeSeverity(type)"
                    />
                  </template>
                </div>
              </div>
            </div>
          </div>

          <!-- Products Section -->
          <div>
            <h3 class="text-xl font-semibold mb-4">Products</h3>
            <DataTable :value="volume.products">
              <Column field="code" header="Code" />
              <Column field="description" header="Description" />
              <Column field="packageType" header="Package Type">
                <template #body="{ data }">
                  <div class="flex flex-wrap gap-1">
                    <template v-for="type in data.packageType.split('_')" :key="type">
                      <Tag :value="type" :severity="getPackageTypeSeverity(type)" />
                    </template>
                  </div>
                </template>
              </Column>
              <Column field="quantity" header="Quantity" />
            </DataTable>
          </div>

          <!-- Sensors Section -->
          <div>
            <h3 class="text-xl font-semibold mb-4">Sensors</h3>
            <DataTable :value="volume.sensors">
              <Column field="code" header="Code" />
              <Column field="type" header="Type">
                <template #body="{ data }">
                  <Tag :value="data.type" :severity="getSensorTypeSeverity(data.type)" />
                </template>
              </Column>
              <Column header="Actions">
                <template #body="{ data }">
                  <div class="flex gap-2">
                    <Button
                      icon="pi pi-chart-line"
                      text
                      rounded
                      severity="info"
                      @click="navigateTo(`/readings?sensor=${data.code}`)"
                      tooltip="View Readings"
                    />
                  </div>
                </template>
              </Column>
            </DataTable>
          </div>
        </div>
      </template>
    </Card>
  </div>
</template>
