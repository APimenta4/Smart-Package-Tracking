<script setup>
import { ref, onMounted, watch } from "vue";
import { useAuthStore } from "../store/auth-store";
import { useRouter } from "vue-router";

const config = useRuntimeConfig();
const api = config.public.API_URL;

const auth = useAuthStore();
const router = useRouter();

const isDark = ref(false);
const showFindDeliveryDialog = ref(false);
const showFindVolumeDialog = ref(false);
const showFindReadingDialog = ref(false);
const showUpdateVolumeStatusDialog = ref(false);
const showSimulateSensorDialog = ref(false);
const searchDeliveryCode = ref("");
const searchVolumeCode = ref("");
const searchReadingCode = ref("");
const updateVolumeCode = ref("");
const newVolumeStatus = ref("");
const sensorCode = ref("");
const sensorType = ref("");
const sensorValue = ref("");

const volumeStatusOptions = [
  "READY_FOR_PICKUP",
  "IN_TRANSIT",
  "DELIVERED",
  "RETURNED",
  "CANCELLED",
];

const sensorTypeOptions = ["ACCELERATION", "TEMPERATURE", "LOCATION"];

function toggleDarkMode() {
  isDark.value = !isDark.value;
  document.documentElement.classList.toggle("app-dark", isDark.value);
  localStorage.setItem("dark-mode", isDark.value ? "enabled" : "disabled");
}

function searchDelivery() {
  if (searchDeliveryCode.value) {
    router.push(`/deliveries/${searchDeliveryCode.value}`);
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

async function simulateSensor() {
  if (
    !validateString(sensorCode.value) ||
    !validateString(sensorType.value) ||
    !validateString(sensorValue.value)
  ) {
    console.error("Sensor Code, Type, and Value are required.");
    return;
  }
  try {
    const payload = { sensorCode: sensorCode.value };
    if (sensorType.value === "ACCELERATION") {
      payload.acceleration = parseFloat(sensorValue.value);
    } else if (sensorType.value === "TEMPERATURE") {
      payload.temperature = parseFloat(sensorValue.value);
    } else if (sensorType.value === "LOCATION") {
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
      throw new Error("Failed to simulate sensor");
    }
    showSimulateSensorDialog.value = false;
    resetSimulateSensorDialog();
  } catch (error) {
    console.error("Failed to simulate sensor:", error);
  }
}

function resetUpdateVolumeStatusDialog() {
  updateVolumeCode.value = "";
  newVolumeStatus.value = "";
}

function resetSimulateSensorDialog() {
  sensorCode.value = "";
  sensorType.value = "";
  sensorValue.value = "";
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
      label: "Deliveries",
      icon: "pi pi-truck",
      items: [
        {
          label: isManager ? "All Deliveries" : "My Deliveries",
          icon: "pi pi-list",
          route: "/deliveries",
          disabled: !auth.isAuthenticated || isLogistician,
        },
        {
          label: "Find Delivery",
          icon: "pi pi-search",
          command: () => (showFindDeliveryDialog.value = true),
          disabled: !auth.isAuthenticated || isLogistician,
        },
        {
          label: "New Delivery",
          icon: "pi pi-plus",
          route: "/deliveries/new",
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
          label: "Add Volume to Delivery",
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
          command: () => (showSimulateSensorDialog.value = true),
          disabled: isLogistician,
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
        <Avatar v-if="auth.isAuthenticated" icon="pi pi-user" />
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
    :header="'Find Delivery'"
    :style="{ width: '90vw', maxWidth: '30rem' }"
  >
    <template #header>
      <div class="flex items-center gap-2">
        <i class="pi pi-truck text-xl"></i>
        <span class="text-xl font-bold">Find Delivery</span>
      </div>
    </template>
    <div class="flex flex-col gap-4">
      <span class="p-float-label">
        <label for="searchDeliveryCode">Delivery Code</label>
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
        <label for="sensorType">Sensor Type</label>
        <Dropdown
          id="sensorType"
          v-model="sensorType"
          :options="sensorTypeOptions"
          class="w-full"
          placeholder="Select a Type"
          :class="{ 'p-invalid': !validateString(sensorType) }"
        />
        <small v-if="!validateString(sensorType)" class="p-error"
          >Sensor Type is required.</small
        >
      </span>
      <span class="p-float-label">
        <label for="sensorValue">Sensor Value</label>
        <InputText
          id="sensorValue"
          v-model="sensorValue"
          class="w-full"
          :class="{ 'p-invalid': !validateString(sensorValue) }"
        />
        <small v-if="!validateString(sensorValue)" class="p-error"
          >Sensor Value is required.</small
        >
      </span>
    </div>

    <template #footer>
      <Button label="Cancel" text @click="showSimulateSensorDialog = false" />
      <Button label="Simulate" @click="simulateSensor" />
    </template>
  </Dialog>
</template>
