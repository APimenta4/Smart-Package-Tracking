<script setup>
import { ref, onMounted } from "vue";

const isDark = ref(false);

function toggleDarkMode() {
  isDark.value = !isDark.value;
  document.documentElement.classList.toggle("app-dark", isDark.value);
  localStorage.setItem("dark-mode", isDark.value ? "enabled" : "disabled");
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
        route: "/deliveries",
      },
    ],
  },
  {
    label: "Volumes",
    icon: "pi pi-box",
    items: [
      {
        label: "All Volumes",
        icon: "pi pi-list",
        route: "/volumes",
      },
      {
        label: "Find Volume",
        icon: "pi pi-search",
        route: "/volumes",
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
        label: "Find Reading",
        icon: "pi pi-search",
        route: "/readings",
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
        <span>John Doe</span>
        <Avatar icon="pi pi-user" />
        <Button icon="pi pi-sign-out" label="Logout" class="p-button-sm" />
        <span
          :class="isDark ? 'pi pi-sun' : 'pi pi-moon'"
          style="font-size: 1.5rem; cursor: pointer"
          @click="toggleDarkMode"
        ></span>
      </div>
    </template>
  </Menubar>
</template>
