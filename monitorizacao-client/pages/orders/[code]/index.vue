<script setup>
import { ref, onMounted } from 'vue';
import { useAuthStore } from '../store/auth-store';
import { useRouter, useRoute } from 'vue-router';
import { useToast } from 'primevue/usetoast';

const authStore = useAuthStore();
const router = useRouter();
const route = useRoute();
const toast = useToast();
const config = useRuntimeConfig();
const api = config.public.API_URL;

const delivery = ref(null);
const error = ref(null);

if (!authStore.isAuthenticated || (authStore.user.role !== "Client" && authStore.user.role !== "Manager")) {
  router.push("/");
}

async function fetchDeliveryDetails() {
  try {
    const response = await fetch(`${api}/orders/${route.params.code}`, {
      headers: {
        'Authorization': `Bearer ${authStore.token}`
      },
    });
    if (!response.ok) {
      const errorText = await response.text();
      let errorMessage = "Failed to fetch order details";
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
    console.error("Failed to fetch order details:", err);
    toast.add({ severity: 'error', summary: 'Error', detail: err.message, life: 3000 });
    error.value = err;
  }
}

onMounted(() => {
  if (authStore.isAuthenticated) {
    fetchDeliveryDetails();
  }
});

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
    READY_FOR_PICKUP: "secondary",
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
          <h2 class="text-2xl font-bold">Order Details</h2>
          <Button
            icon="pi pi-arrow-left"
            label="Back to Orders"
            text
            @click="navigateTo('/orders')"
          />
        </div>
      </template>
      <template #content>
        <!-- Basic Delivery Info -->
        <div class="grid grid-cols-2 gap-4 mb-6">
          <div class="flex flex-col">
            <span class="text-sm font-medium">Order Code</span>
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
                  products
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
                  sensors
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