<script setup>
import { ref, onMounted } from "vue";
import { useAuthStore } from "../store/auth-store";
import { useRouter } from "vue-router";

const auth = useAuthStore();
const router = useRouter();

const isDark = ref(false);
const showFindDeliveryDialog = ref(false);
const showFindVolumeDialog = ref(false);
const showFindReadingDialog = ref(false);
const searchDeliveryCode = ref("");
const searchVolumeCode = ref("");
const searchReadingCode = ref("");

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

const items = ref([
  {
    label: "Deliveries",
    icon: "pi pi-truck",
    items: [
      {
        label: "New Delivery",
        icon: "pi pi-plus",
        route: "/deliveries/new",
      },
      {
        label: "All Deliveries",
        icon: "pi pi-list",
        route: "/deliveries",
      },
      {
        label: "Find Delivery",
        icon: "pi pi-search",
        command: () => showFindDeliveryDialog.value = true
      },
    ],
  },
  {
    label: "Volumes",
    icon: "pi pi-box",
    items: [
      {
        label: "Add Volume to Delivery",
        icon: "pi pi-plus",
        route: "/volumes/new",
      },
      {
        label: "All Volumes",
        icon: "pi pi-list",
        route: "/volumes",
      },
      {
        label: "Find Volume",
        icon: "pi pi-search",
        command: () => showFindVolumeDialog.value = true
      },
    ],
  },
  {
    label: "Readings",
    icon: "pi pi-history",
    items: [
      {
        label: "All Readings",
        icon: "pi pi-list",
        route: "/readings",
      },
      {
        label: "Find Readings by Sensor",
        icon: "pi pi-search",
        command: () => showFindReadingDialog.value = true
      },
    ],
  },
]);

onMounted(() => {
  const darkMode = localStorage.getItem("dark-mode");
  isDark.value =
    darkMode === "enabled" ||
    (!darkMode && window.matchMedia("(prefers-color-scheme: dark)").matches);
  if (isDark.value) {
    document.documentElement.classList.add("app-dark");
  }
});
</script>

<template>
  <NuxtLoadingIndicator />
  <Menubar :model="items" class="border-t-0 border-l-0 border-r-0 rounded-none h-14">
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
</template>