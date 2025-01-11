<script setup>
import { ref, onMounted } from "vue";
const route = useRoute();

const config = useRuntimeConfig();
const api = config.public.API_URL;

const delivery = ref({});

async function fetchDelivery() {
  try {
    const response = await fetch(`${api}/orders/${route.params.code}`);
    const data = await response.json();
    delivery.value = data;
  } catch (error) {
    console.error("Failed to fetch delivery:", error);

  }
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

onMounted(() => {
  fetchDelivery();
});
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
            <span class="text-xl">{{ delivery.code }}</span>
          </div>
          <div class="flex flex-col">
            <span class="text-sm font-medium">Client Code</span>
            <span class="text-xl">{{ delivery.clientCode }}</span>
          </div>
        </div>

        <!-- Volumes Section -->
        <div class="mb-4">
          <h3 class="text-xl font-semibold mb-4">Volumes</h3>
          <DataTable :value="delivery.volumes">
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
                {{ data.products.length }} products
              </template>
            </Column>
            <Column header="Sensors">
              <template #body="{ data }">
                {{ data.sensors.length }} sensors
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
    <Toast />
  </div>
</template>
