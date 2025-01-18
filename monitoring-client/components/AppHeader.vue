<script setup>
import { ref, onMounted, watch } from "vue";
import { useAuthStore } from "../store/auth-store";
import { useRouter } from "vue-router";
import { useToast } from "primevue/usetoast"; 

const config = useRuntimeConfig();
const api = config.public.API_URL;

const auth = useAuthStore();
const router = useRouter();

const toast = useToast();

const isDark = ref(false);
const showFindDeliveryDialog = ref(false);
const showFindVolumeDialog = ref(false);
const showFindReadingDialog = ref(false);
const showFindSensorDialog = ref(false);
const showUpdateVolumeStatusDialog = ref(false);
const showSimulateSensorDialog = ref(false);
const showLocation = ref(false); 
const showTemperature = ref(false); 
const showAcceleration = ref(false);

const searchDeliveryCode = ref("");
const searchVolumeCode = ref("");
const searchReadingCode = ref("");
const updateVolumeCode = ref("");
const newVolumeStatus = ref("");
const sensorCode = ref("");
const sensor = ref(null);

const temperature = ref("");
const acceleration = ref("");
const latitude = ref("");
const longitude = ref("");

const volumeStatusOptions = [
  { label: "In Transit", value: "IN_TRANSIT" },
  { label: "Delivered", value: "DELIVERED" },
  { label: "Returned", value: "RETURNED" },
  { label: "Cancelled", value: "CANCELLED" },
];

async function findSensor(){
  if (!validateString(sensorCode.value)) {
    console.error("Sensor Code are required.");
    toast.add({ severity: 'error', summary: 'Error', detail: 'Sensor Code are required.', life: 3000 });
    return;
  }
  try {
    const sensorResponse = await fetch(`${api}/sensors/${sensorCode.value}`);
    if (!sensorResponse.ok) {
      throw new Error("Failed to fetch sensor details");
    }
    sensor.value = await sensorResponse.json();

    showFindSensorDialog.value = false;
    showAcceleration.value = sensor.value.type === "ACCELERATION";
    showTemperature.value = sensor.value.type === "TEMPERATURE";
    showLocation.value = sensor.value.type === "LOCATION";
    console.log(showLocation.value)
    console.log(showTemperature.value)
    console.log(showAcceleration.value)
    showSimulateSensorDialog.value = true;

  }catch (error){
    console.error("Failed to find sensor:", error);
    toast.add({ severity: 'error', summary: 'Error', detail: error.message, life: 3000 });
  }
}

function resetFindSensorDialog() {
  sensorCode.value = "";
}

async function simulateSensor() {
  if (sensor === null) {
    console.error("Failed to find sensor:", error);
    toast.add({ severity: 'error', summary: 'Error', detail: error.message, life: 3000 });
    return;
  }
  try {
    const payload = { sensorCode: sensor.value.code };


    if (sensor.value.type === "ACCELERATION") {
      payload.acceleration = parseFloat(acceleration.value);
    } else if (sensor.value.type === "TEMPERATURE") {
      payload.temperature = parseFloat(temperature.value);
    } else if (sensor.value.type === "LOCATION") {
      payload.latitude = parseFloat(latitude.value);
      payload.longitude = parseFloat(longitude.value);
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
    toast.add({ severity: 'success', summary: 'Success', detail: 'Sensor simulated successfully', life: 3000 });
    showSimulateSensorDialog.value = false;
    resetSimulateSensorDialog();
  } catch (error) {
    console.error("Failed to simulate sensor:", error);
    toast.add({ severity: 'error', summary: 'Error', detail: error.message, life: 3000 });
  }
}

function resetSimulateSensorDialog() {
  showAcceleration.value = false;
  showLocation.value = false;
  showTemperature.value = false;
  temperature.value ="";
  acceleration.value ="";
  latitude.value ="";
  longitude.value ="";
}

const sensorTypeOptions = ["ACCELERATION (G)", "TEMPERATURE (ºC)", "LOCATION (LATITUDE,LONGITUDE)"];

function toggleDarkMode() {
  isDark.value = !isDark.value;
  document.documentElement.classList.toggle("app-dark", isDark.value);
  localStorage.setItem("dark-mode", isDark.value ? "enabled" : "disabled");
}

function searchDelivery() {
  if (searchDeliveryCode.value) {
    router.push(`/orders/${searchDeliveryCode.value}`);
    searchDeliveryCode.value = "";
    showFindDeliveryDialog.value = false;
  }
}

function searchVolume() {
  if (searchVolumeCode.value) {
    router.push(`/volumes/${searchVolumeCode.value}`);
    searchVolumeCode.value = "";
    showFindVolumeDialog.value = false;
  }
}

function searchReading() {
  if (searchReadingCode.value) {
    router.push(`/readings/${searchReadingCode.value}`);
    searchReadingCode.value = "";
    showFindReadingDialog.value = false;
  }
}

async function updateVolumeStatus() {
  if (
    !validateString(updateVolumeCode.value) ||
    !validateString(newVolumeStatus.value)
  ) {
    console.error("Volume Code and New Status are required.");
    toast.add({ severity: 'error', summary: 'Error', detail: 'Volume Code and New Status are required.', life: 3000 }); 
    return;
  }
  try {
    const response = await fetch(`${api}/volumes/${updateVolumeCode.value}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
        'Authorization': `Bearer ${auth.token}` 
      },
      body: JSON.stringify({
        status: newVolumeStatus.value,
      }),
    });
    if (!response.ok) {
      const errorText = await response.text();
      let errorMessage = "Failed to update volume status";
      try {
        const errorData = JSON.parse(errorText);
        errorMessage = errorData.message || errorMessage;
      } catch (e) {
        errorMessage = errorText;
      }
      throw new Error(errorMessage);
    }
    toast.add({ severity: 'success', summary: 'Success', detail: 'Volume status updated successfully', life: 3000 }); 
    showUpdateVolumeStatusDialog.value = false;
    resetUpdateVolumeStatusDialog();
  } catch (error) {
    console.error("Failed to update volume status:", error);
    toast.add({ severity: 'error', summary: 'Error', detail: error.message, life: 3000 }); 
  }
}

function resetUpdateVolumeStatusDialog() {
  updateVolumeCode.value = "";
  newVolumeStatus.value = "";
}

const validateString = (value) =>
  typeof value === "string" && value.trim().length > 0;

const isLogistician = auth.user && auth.user.role === "Logistician";

const items = ref([]);

const updateMenuItems = () => {
  const isManager = auth.user && auth.user.role === "Manager";
  const isClient = auth.user && auth.user.role === "Client";
  const isLogistician = auth.user && auth.user.role === "Logistician";

  items.value = [
    {
      label: "Orders",
      icon: "pi pi-truck",
      items: [
        {
          label: isManager ? "All Orders" : "My Orders",
          icon: "pi pi-list",
          route: "/orders",
          disabled: !auth.isAuthenticated || isLogistician,
        },
        {
          label: "Find Order",
          icon: "pi pi-search",
          command: () => (showFindDeliveryDialog.value = true),
          disabled: !auth.isAuthenticated || isLogistician,
        },
        {
          label: "New Order",
          icon: "pi pi-plus",
          route: "/orders/new",
          disabled: !isLogistician,
        },
      ],
    },
    {
      label: "Volumes",
      icon: "pi pi-box",
      items: [
        {
          label: isManager ? "All Volumes" : "My Volumes",
          icon: "pi pi-list",
          route: "/volumes",
          disabled: !auth.isAuthenticated || isLogistician,
        },
        {
          label: "Find Volume",
          icon: "pi pi-search",
          command: () => (showFindVolumeDialog.value = true),
          disabled: !auth.isAuthenticated || isLogistician,
        },
        {
          label: "Update Volume Status",
          icon: "pi pi-refresh",
          command: () => (showUpdateVolumeStatusDialog.value = true),
          disabled: !isLogistician,
        },
        {
          label: "Add Volume to Order",
          icon: "pi pi-plus",
          route: "/volumes/new",
          disabled: !isLogistician,
        }
      ],
    },
    {
      label: "Readings",
      icon: "pi pi-history",
      items: [
        {
          label: isManager ? "All Readings" : "My Readings",
          icon: "pi pi-list",
          route: "/readings",
          disabled: !auth.isAuthenticated || isLogistician,
        },
        {
          label: "Find Readings by Sensor",
          icon: "pi pi-search",
          command: () => (showFindReadingDialog.value = true),
          disabled: !auth.isAuthenticated || isLogistician,
        },
        {
          label: "Simulate a Sensor",
          icon: "pi pi-cog",
          command: () => (showFindSensorDialog.value = true),
        },
      ],
    },
  ];
};

watch(
  () => auth.user,
  () => {
    updateMenuItems();
  },
  { immediate: true }
);

onMounted(() => {
  const darkMode = localStorage.getItem("dark-mode");
  isDark.value =
    darkMode === "enabled" ||
    (!darkMode && window.matchMedia("(prefers-color-scheme: dark)").matches);
  if (isDark.value) {
    document.documentElement.classList.add("app-dark");
  }
  updateMenuItems();
});
</script>

<template>
  <NuxtLoadingIndicator />
  <Menubar
    :model="items"
    class="border-t-0 border-l-0 border-r-0 rounded-none h-14"
  >
    <template v-slot:start>
      <router-link to="/">
        <img src="/logo.png" alt="Logo" class="h-10 inline-block mr-2" />
      </router-link>
    </template>
    <template v-slot:item="{ item, props, hasSubmenu }">
      <router-link
        v-if="item.route"
        v-slot="{ href, navigate }"
        :to="item.route"
        custom
      >
        <a v-ripple :href="href" v-bind="props.action" @click="navigate">
          <span :class="item.icon" />
          <span>{{ item.label }}</span>
        </a>
      </router-link>
      <a
        v-else
        v-ripple
        :href="item.url"
        :target="item.target"
        v-bind="props.action"
      >
        <span :class="item.icon" />
        <span>{{ item.label }}</span>
        <span v-if="hasSubmenu" class="pi pi-fw pi-angle-down" />
      </a>
    </template>
    <template v-slot:end>
      <div class="flex items-center gap-4">
        <span v-if="auth.isAuthenticated">{{ auth.user?.name }}</span>
        <router-link to="/profile">
          <Avatar v-if="auth.isAuthenticated" icon="pi pi-user" class="cursor-pointer" />
        </router-link>
        <Button
          v-if="auth.isAuthenticated"
          icon="pi pi-sign-out"
          label="Logout"
          class="p-button-sm"
          @click="auth.logout"
        />
        <Button
          v-else
          icon="pi pi-sign-in"
          label="Login"
          class="p-button-sm"
          @click="router.push('/login')"
        />
        <span
          :class="isDark ? 'pi pi-sun' : 'pi pi-moon'"
          style="font-size: 1.5rem; cursor: pointer"
          @click="toggleDarkMode"
        ></span>
      </div>
    </template>
  </Menubar>

  <Dialog
    v-model:visible="showFindDeliveryDialog"
    modal
    :header="'Find Order'"
    :style="{ width: '90vw', maxWidth: '30rem' }"
  >
    <template #header>
      <div class="flex items-center gap-2">
        <i class="pi pi-truck text-xl"></i>
        <span class="text-xl font-bold">Find Order</span>
      </div>
    </template>
    <div class="flex flex-col gap-4">
      <span class="p-float-label">
        <label for="searchDeliveryCode">Order Code</label>
        <InputText
          id="searchDeliveryCode"
          v-model="searchDeliveryCode"
          class="w-full"
          @keyup.enter="searchDelivery"
        />
      </span>
    </div>

    <template #footer>
      <Button label="Cancel" text @click="showFindDeliveryDialog = false" />
      <Button label="Find" @click="searchDelivery" />
    </template>
  </Dialog>

  <Dialog
    v-model:visible="showFindVolumeDialog"
    modal
    :header="'Find Volume'"
    :style="{ width: '90vw', maxWidth: '30rem' }"
  >
    <template #header>
      <div class="flex items-center gap-2">
        <i class="pi pi-box text-xl"></i>
        <span class="text-xl font-bold">Find Volume</span>
      </div>
    </template>
    <div class="flex flex-col gap-4">
      <span class="p-float-label">
        <label for="searchVolumeCode">Volume Code</label>
        <InputText
          id="searchVolumeCode"
          v-model="searchVolumeCode"
          class="w-full"
          @keyup.enter="searchVolume"
        />
      </span>
    </div>

    <template #footer>
      <Button label="Cancel" text @click="showFindVolumeDialog = false" />
      <Button label="Find" @click="searchVolume" />
    </template>
  </Dialog>

  <Dialog
    v-model:visible="showFindReadingDialog"
    modal
    :header="'Find Reading'"
    :style="{ width: '90vw', maxWidth: '30rem' }"
  >
    <template #header>
      <div class="flex items-center gap-2">
        <i class="pi pi-history text-xl"></i>
        <span class="text-xl font-bold">Find Readings</span>
      </div>
    </template>
    <div class="flex flex-col gap-4">
      <span class="p-float-label">
        <label for="searchReadingCode">Sensor Code</label>
        <InputText
          id="searchReadingCode"
          v-model="searchReadingCode"
          class="w-full"
          @keyup.enter="searchReading"
        />
      </span>
    </div>

    <template #footer>
      <Button label="Cancel" text @click="showFindReadingDialog = false" />
      <Button label="Find" @click="searchReading" />
    </template>
  </Dialog>

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
          optionLabel="label"
          optionValue="value"
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
    v-model:visible="showFindSensorDialog"
    modal
    :header="'Simulate a Sensor'"
    :style="{ width: '90vw', maxWidth: '30rem' }"
    @hide="resetFindSensorDialog"
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
    </div>

    <template #footer>
      <Button label="Cancel" text @click="showFindSensorDialog = false" />
      <Button label="Find" @click="findSensor" />
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
        <span class="text-xl font-bold">Simulate a Reading for {{ sensor?.code }}</span>
      </div>
    </template>
    <div class="flex flex-col gap-4">
      <span v-if="showLocation" class="p-float-label">
        <label for="latitude">Latitude</label>
        <InputText id="latitude" v-model="latitude" class="w-full" />
      </span>
      <span v-if="showLocation" class="p-float-label">
        <label for="longitude">Longitude</label>
        <InputText id="longitude" v-model="longitude" class="w-full" />
      </span>
      <span v-if="showTemperature"class="p-float-label">
        <label for="temperature">Temperature (ºC)</label>
        <InputText id="temperature" v-model="temperature" class="w-full" />
      </span>
      <span v-if="showAcceleration" class="p-float-label">
        <label for="acceleration">Acceleration (G)</label>
        <InputText id="acceleration" v-model="acceleration" class="w-full" />
      </span>
    </div>

    <template #footer>
      <Button label="Cancel" text @click="showSimulateSensorDialog = false" />
      <Button label="Simulate" @click="simulateSensor" />
    </template>
  </Dialog>
</template>
