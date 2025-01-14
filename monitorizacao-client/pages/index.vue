<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "../store/auth-store";
import { useToast } from "primevue/usetoast"; // Import useToast

const toast = useToast(); // Initialize toast

const auth = useAuthStore();

const config = useRuntimeConfig();
const api = config.public.API_URL;

const router = useRouter();

const isLogistician = auth.user && auth.user.role === "Logistician";

const features = [
  {
    description: "Simulate Sensor Reading",
    icon: "sensors",
    action: () => (showSimulateSensorDialog.value = true),
    disabled: false,
  },
  {
    description: "Simulate New Delivery",
    icon: "local_shipping",
    action: () => router.push("/deliveries/new"),
    disabled: !isLogistician,
  },
  {
    description: "Update Volume Status",
    icon: "refresh",
    action: () => (showUpdateVolumeStatusDialog.value = true),
    disabled: !isLogistician,
  },
  {
    description: "Add Volume to Delivery",
    icon: "add_box",
    action: () => router.push("/volumes/new"),
    disabled: !isLogistician,
  }
];

const responsiveOptions = ref([
  {
    breakpoint: "1400px",
    numVisible: 2,
    numScroll: 1,
  },
  {
    breakpoint: "1199px",
    numVisible: 3,
    numScroll: 1,
  },
  {
    breakpoint: "767px",
    numVisible: 2,
    numScroll: 1,
  },
  {
    breakpoint: "575px",
    numVisible: 1,
    numScroll: 1,
  },
]);

const hoveredItem = ref(null);

const showUpdateVolumeStatusDialog = ref(false);
const updateVolumeCode = ref("");
const newVolumeStatus = ref("");

const volumeStatusOptions = [
  "READY_FOR_PICKUP",
  "IN_TRANSIT",
  "DELIVERED",
  "RETURNED",
  "CANCELLED",
];

const validateString = (value) =>
  typeof value === "string" && value.trim().length > 0;

async function updateVolumeStatus() {
  if (
    !validateString(updateVolumeCode.value) ||
    !validateString(newVolumeStatus.value)
  ) {
    console.error("Volume Code and New Status are required.");
    return;
  }
  try {
    const response = await fetch(`${api}/volumes/${updateVolumeCode.value}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        status: newVolumeStatus.value,
      }),
    });
    if (!response.ok) {
      throw new Error("Failed to update volume status");
    }
    showUpdateVolumeStatusDialog.value = false;
    resetUpdateVolumeStatusDialog();
  } catch (error) {
    console.error("Failed to update volume status:", error);
  }
}

function resetUpdateVolumeStatusDialog() {
  updateVolumeCode.value = "";
  newVolumeStatus.value = "";
}

const showSimulateSensorDialog = ref(false);
const sensorCode = ref("");
const sensorType = ref("");
const sensorValue = ref("");

const sensorTypeOptions = ["ACCELERATION (m/s²)", "TEMPERATURE (ºC)", "LOCATION (LATITUDE,LONGITUDE)"];

async function simulateSensor() {
  if (
    !validateString(sensorCode.value) ||
    !validateString(sensorType.value) ||
    !validateString(sensorValue.value)
  ) {
    console.error("Sensor Code, Type, and Value are required.");
    toast.add({ severity: 'error', summary: 'Error', detail: 'Sensor Code, Type, and Value are required.', life: 3000 }); // Show error toast
    return;
  }
  try {
    const payload = { sensorCode: sensorCode.value };
    if (sensorType.value === "ACCELERATION (m/s²)") {
      payload.acceleration = parseFloat(sensorValue.value);
    } else if (sensorType.value === "TEMPERATURE (ºC)") {
      payload.temperature = parseFloat(sensorValue.value);
    } else if (sensorType.value === "LOCATION (LATITUDE,LONGITUDE)") {
      const [latitude, longitude] = sensorValue.value.split(",").map(Number);
      payload.latitude = latitude;
      payload.longitude = longitude;
    }
    const response = await fetch(`${api}/readings`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    });
    if (!response.ok) {
      const errorText = await response.text();
      let errorMessage = "Failed to simulate sensor";
      try {
        const errorData = JSON.parse(errorText);
        errorMessage = errorData.message || errorMessage;
      } catch (e) {
        errorMessage = errorText;
      }
      throw new Error(errorMessage);
    }
    toast.add({ severity: 'success', summary: 'Success', detail: 'Sensor simulated successfully', life: 3000 }); // Show success toast
    showSimulateSensorDialog.value = false;
    resetSimulateSensorDialog();
  } catch (error) {
    console.error("Failed to simulate sensor:", error);
    toast.add({ severity: 'error', summary: 'Error', detail: error.message, life: 3000 }); // Show error toast with details
  }
}

function resetSimulateSensorDialog() {
  sensorCode.value = "";
  sensorType.value = "";
  sensorValue.value = "";
}
</script>

<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow justify-center mb-12">
    <Carousel
      :value="features"
      :numVisible="3"
      :numScroll="1"
      :responsiveOptions="responsiveOptions"
      circular
      :autoplayInterval="7000"
    >
      <template #item="slotProps">
        <div
          class="border border-surface-200 dark:border-surface-700 rounded m-2.5 p-12 flex flex-col items-center group transition-transform duration-300 transform hover:scale-105"
          :class="{
            'opacity-60':
              hoveredItem !== null &&
              hoveredItem.description !== slotProps.data.description,
            'cursor-not-allowed opacity-50': slotProps.data.disabled,
            'hover-pointer': !slotProps.data.disabled,
          }"
          :style="{ pointerEvents: slotProps.data.disabled ? 'none' : 'auto' }"
          @mouseover="hoveredItem = slotProps.data"
          @mouseleave="hoveredItem = null"
          @click="
            !slotProps.data.disabled &&
              slotProps.data.action &&
              slotProps.data.action()
          "
        >
          <div class="mb-4 font-medium text-center text-xl">
            {{ slotProps.data.description }}
          </div>
          <div class="flex-grow flex items-center justify-center">
            <span
              class="material-icons-outlined !text-5xl transition-all duration-300 group-hover:text-orange-500 group-hover:scale-110"
            >
              {{ slotProps.data.icon }}
            </span>
          </div>
        </div>
      </template>
    </Carousel>

    <Dialog
      v-model:visible="showUpdateVolumeStatusDialog"
      modal
      :header="'Update Volume Status'"
      :style="{ width: '90vw', maxWidth: '30rem' }"
      @hide="resetUpdateVolumeStatusDialog"
    >
      <template #header>
        <div class="flex items-center gap-2">
          <i class="pi pi-refresh text-xl"></i>
          <span class="text-xl font-bold">Update Volume Status</span>
        </div>
      </template>
      <div class="flex flex-col gap-4">
        <span class="p-float-label">
          <label for="updateVolumeCode">Volume Code</label>
          <InputText
            id="updateVolumeCode"
            v-model="updateVolumeCode"
            class="w-full"
            :class="{ 'p-invalid': !validateString(updateVolumeCode) }"
          />
          <small v-if="!validateString(updateVolumeCode)" class="p-error"
            >Volume Code is required.</small
          >
        </span>
        <span class="p-float-label">
          <label for="newVolumeStatus">New Status</label>
          <Dropdown
            id="newVolumeStatus"
            v-model="newVolumeStatus"
            :options="volumeStatusOptions"
            class="w-full"
            placeholder="Select a Status"
            :class="{ 'p-invalid': !validateString(newVolumeStatus) }"
          />
          <small v-if="!validateString(newVolumeStatus)" class="p-error"
            >New Status is required.</small
          >
        </span>
      </div>

      <template #footer>
        <Button
          label="Cancel"
          text
          @click="showUpdateVolumeStatusDialog = false"
        />
        <Button label="Update" @click="updateVolumeStatus" />
      </template>
    </Dialog>

    <Dialog
      v-model:visible="showSimulateSensorDialog"
      modal
      :header="'Simulate a Sensor'"
      :style="{ width: '90vw', maxWidth: '30rem' }"
      @hide="resetSimulateSensorDialog"
    >
      <template #header>
        <div class="flex items-center gap-2">
          <i class="pi pi-cog text-xl"></i>
          <span class="text-xl font-bold">Simulate a Reading</span>
        </div>
      </template>
      <div class="flex flex-col gap-4">
        <span class="p-float-label">
          <label for="sensorCode">Sensor Code</label>
          <InputText
            id="sensorCode"
            v-model="sensorCode"
            class="w-full"
            :class="{ 'p-invalid': !validateString(sensorCode) }"
          />
          <small v-if="!validateString(sensorCode)" class="p-error"
            >Sensor Code is required.</small
          >
        </span>
        <span class="p-float-label">
          <label for="sensorType">Reading Type</label>
          <Dropdown
            id="sensorType"
            v-model="sensorType"
            :options="sensorTypeOptions"
            class="w-full"
            placeholder="Select a Type"
            :class="{ 'p-invalid': !validateString(sensorType) }"
          />
          <small v-if="!validateString(sensorType)" class="p-error"
            >Reading Type is required.</small
          >
        </span>
        <span class="p-float-label">
          <label for="sensorValue">Reading Value</label>
          <InputText
            id="sensorValue"
            v-model="sensorValue"
            class="w-full"
            :class="{ 'p-invalid': !validateString(sensorValue) }"
          />
          <small v-if="!validateString(sensorValue)" class="p-error"
            >Reading Value is required.</small
          >
        </span>
      </div>

      <template #footer>
        <Button label="Cancel" text @click="showSimulateSensorDialog = false" />
        <Button label="Simulate" @click="simulateSensor" />
      </template>
    </Dialog>
  </div>
</template>

<style scoped>
.hover-pointer {
  cursor: pointer;
}
</style>
