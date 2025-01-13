<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute();
const deliveryCode = route.params.code;

const config = useRuntimeConfig();
const api = config.public.API_URL;

const volumes = ref([]);
const isDetailsDialogVisible = ref(false);
const selectedDetails = ref({
  title: "",
  items: [],
});

async function fetchVolumes() {
  try {
    const response = await fetch(`${api}/orders/${deliveryCode}/volumes`);
    const data = await response.json();
    volumes.value = data;
  } catch (error) {
    console.error("Failed to fetch volumes:", error);
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

onMounted(() => {
  fetchVolumes();
});
</script>
<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow mb-12">
    <Card class="mt-10">
      <template #title>
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-bold">Delivery {{ deliveryCode }} Volumes</h2>
          <Button
            icon="pi pi-arrow-left"
            label="Back to Deliveries"
            text
            @click="navigateTo('/deliveries')"
          />       
        </div>
      </template>
      <template #content>
        <DataTable
          :value="volumes"
          paginator
          :rows="10"
          tableStyle="min-width: 50rem"
        >
          <Column field="code" header="Volume Code" sortable />
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
              </div>
            </template>
          </Column>
        </DataTable>
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