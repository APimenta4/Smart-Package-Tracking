<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { useAuthStore } from "../store/auth-store";
import { useToast } from "primevue/usetoast";

const config = useRuntimeConfig();
const api = config.public.API_URL;

const router = useRouter();
const toast = useToast();
const route = useRoute();
const authStore = useAuthStore();

if (
  !authStore.isAuthenticated ||
  (authStore.user.role !== "Client" && authStore.user.role !== "Manager")
) {
  router.push("/");
}

const volume = ref(null);
const timelineEvents = ref([]);

const statusSeverity = {
  READY_FOR_PICKUP: "secondary",
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
        Authorization: `Bearer ${authStore.token}`,
      },
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
    timelineEvents.value = [
      {
        status: "Ready for Pickup",
        date: data.readyDate
          ? new Date(data.readyDate.replace("[UTC]", ""))
          : null,
        icon: "pi pi-info-circle",
        color: "#2db7f5",
      },
      {
        status: "Shipped",
        date: data.shippedDate
          ? new Date(data.shippedDate.replace("[UTC]", ""))
          : null,
        icon: "pi pi-truck",
        color: "#ff9800",
      },
      {
        status: "Delivered",
        date: data.deliveredDate
          ? new Date(data.deliveredDate.replace("[UTC]", ""))
          : null,
        icon: "pi pi-check",
        color: "#28a745",
      },
      {
        status: "Returned",
        date: data.returnedDate
          ? new Date(data.returnedDate.replace("[UTC]", ""))
          : null,
        icon: "pi pi-undo",
        color: "#ffc107",
      },
      {
        status: "Cancelled",
        date: data.cancelledDate
          ? new Date(data.cancelledDate.replace("[UTC]", ""))
          : null,
        icon: "pi pi-times",
        color: "#dc3545",
      },
    ].filter((event) => event.date);
  } catch (error) {
    console.error("Failed to fetch volume:", error);
    toast.add({
      severity: "error",
      summary: "Error",
      detail: error.message,
      life: 3000,
    });
  }
}

onMounted(() => {
  if (authStore.isAuthenticated) {
    fetchVolume();
  }
  setTimeout(() => {
    console.log(timelineEvents.value);
  }, 2000);
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
                <span class="text-sm font-medium">Order Code</span>
                <div class="text-xl">
                  <Button
                    link
                    class="text-blue-600 hover:text-blue-800 p-0 text-xl"
                    @click="navigateTo(`/orders/${volume.orderCode}`)"
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
                  <Tag
                    :value="data.type"
                    :severity="getSensorTypeSeverity(data.type)"
                  />
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

          <!-- Timeline Section -->
          <div>
            <h3 class="text-xl font-semibold mb-10">Volume Tracking</h3>
            <Timeline :value="timelineEvents">
              <template #opposite="{ item }">
                <span>{{ item.date ? item.date.toLocaleString() : "" }}</span>
              </template>
              <template #marker="{ item }">
                <span
                  class="p-shadow-2"
                  :style="{
                    backgroundColor: item.color,
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    width: '2rem',
                    height: '2rem',
                    borderRadius: '50%',
                    color: '#fff',
                  }"
                >
                  <i :class="item.icon"></i>
                </span>
              </template>
              <template #content="{ item }">
                <h4 :style="{ fontSize: '1.25rem' }">{{ item.status }}</h4>
              </template>
            </Timeline>
          </div>
        </div>
      </template>
    </Card>
  </div>
</template>
