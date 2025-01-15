<script setup>
import { useAuthStore } from '../store/auth-store'; // Import auth store

const route = useRoute();
const config = useRuntimeConfig();
const toast = useToast();
const auth = useAuthStore(); // Use auth store

const delivery = ref(null);
const error = ref(null);

try {
  const response = await fetch(`${config.public.API_URL}/orders/${route.params.code}`, {
    headers: {
      'Authorization': `Bearer ${auth.token}` // Add auth token to headers
    },
  });
  if (!response.ok) {
    const errorText = await response.text();
    let errorMessage = "Failed to fetch delivery details";
    try {
      const errorData = JSON.parse(errorText);
      errorMessage = errorData.message || errorMessage;
    } catch (e) {
      errorMessage = errorText;
    }
    throw new Error(errorMessage);
  }
  delivery.value = await response.json();
} catch (err) {
  console.error("Failed to fetch delivery details:", err);
  toast.add({ severity: 'error', summary: 'Error', detail: err.message, life: 3000 }); // Show error toast with details
  error.value = err;
}

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
  const severityMap = {
    READY_FOR_PICKUP: "info",
    IN_TRANSIT: "info",
    DELIVERED: "success",
    RETURNED: "warning",
    CANCELLED: "danger",
  };
  return severityMap[status] || "secondary";
};

const isDetailsDialogVisible = ref(false);
const selectedDetails = ref({
  title: "",
  items: [],
});

const showProductDetails = (products) => {
  selectedDetails.value = {
    title: "Products Details",
    items: products.map((p) => ({
      code: p.code,
      description: `Quantity: ${p.quantity}`,
    })),
  };
  isDetailsDialogVisible.value = true;
};

const showSensorDetails = (sensors) => {
  selectedDetails.value = {
    title: "Sensors Details",
    items: sensors.map((s) => ({
      code: s.code,
      description: `Type: ${s.type}`,
    })),
  };
  isDetailsDialogVisible.value = true;
};
</script>

<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow mb-12">
    <Card class="mt-10">
      <template #title>
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-bold">Delivery Details</h2>
          <Button
            icon="pi pi-arrow-left"
            label="Back to Deliveries"
            text
            @click="navigateTo('/deliveries')"
          />
        </div>
      </template>
      <template #content>
        <!-- Basic Delivery Info -->
        <div class="grid grid-cols-2 gap-4 mb-6">
          <div class="flex flex-col">
            <span class="text-sm font-medium">Delivery Code</span>
            <span class="text-xl">{{ delivery?.code }}</span>
          </div>
          <div class="flex flex-col">
            <span class="text-sm font-medium">Client Code</span>
            <span class="text-xl">{{ delivery?.clientCode }}</span>
          </div>
        </div>

        <!-- Volumes Section -->
        <div class="mb-4">
          <h3 class="text-xl font-semibold mb-4">Volumes</h3>
          <DataTable :value="delivery?.volumes">
            <Column field="code" header="Code" />
            <Column field="packageType" header="Package Type">
              <template #body="{ data }">
                <div class="flex flex-wrap gap-1">
                  <template
                    v-for="type in data.packageType.split('_')"
                    :key="type"
                  >
                    <Tag
                      :value="type"
                      :severity="getPackageTypeSeverity(type)"
                    />
                  </template>
                </div>
              </template>
            </Column>
            <Column field="status" header="Status">
              <template #body="{ data }">
                <Tag :value="data.status.replace(/_/g, ' ')" :severity="getStatusSeverity(data.status)" />
              </template>
            </Column>
            <Column header="Products">
              <template #body="{ data }">
                <Button
                  link
                  class="text-blue-600 hover:text-blue-800 p-0"
                  @click="showProductDetails(data.products)"
                >
                  {{ data.products.length }} products
                </Button>
              </template>
            </Column>
            <Column header="Sensors">
              <template #body="{ data }">
                <Button
                  link
                  class="text-blue-600 hover:text-blue-800 p-0"
                  @click="showSensorDetails(data.sensors)"
                >
                  {{ data.sensors.length }} sensors
                </Button>
              </template>
            </Column>
            <Column header="Actions">
              <template #body="{ data }">
                <div class="flex gap-2">
                  <Button
                    icon="pi pi-eye"
                    text
                    rounded
                    severity="info"
                    @click="navigateTo(`/volumes/${data.code}`)"
                    tooltip="View Volume Details"
                  />
                  <Button
                    icon="pi pi-chart-line"
                    text
                    rounded
                    severity="success"
                    @click="navigateTo(`/volumes/${data.code}/readings`)"
                    tooltip="View Readings"
                  />
                </div>
              </template>
            </Column>
          </DataTable>
        </div>
      </template>
    </Card>
  </div>

  <Dialog
    v-model:visible="isDetailsDialogVisible"
    :header="selectedDetails.title"
    modal
    :style="{ width: '450px' }"
  >
    <DataTable :value="selectedDetails.items" :rows="5">
      <Column field="code" header="Code">
        <template #body="{ data }">
          <span>{{ data.code }}</span>
        </template>
      </Column>
      <Column field="description" header="Description" />
    </DataTable>
  </Dialog>
</template>