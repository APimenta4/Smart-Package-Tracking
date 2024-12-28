
<script setup>
import { ref, onMounted } from "vue";

const isDark = ref(false);

function toggleDarkMode() {
  isDark.value = !isDark.value;
  document.documentElement.classList.toggle("app-dark", isDark.value);
  localStorage.setItem("dark-mode", isDark.value ? "enabled" : "disabled");
}

const items = ref([
  { label: "New", icon: "pi pi-fw pi-plus" },
  { label: "Delete", icon: "pi pi-fw pi-trash" },
  { label: "Refresh", icon: "pi pi-fw pi-refresh" },
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
  <Menubar
    :model="items"
    class="border-t-0 border-l-0 border-r-0 rounded-none h-14"
  >
    <template v-slot:start> Logo or name </template>
    <template v-slot:end>
      <div class="flex items-center gap-4">
        <span class="text-blue-500">Funcionário Logística</span>
        <span class="text-green-500">Gestor</span>
        <span class="text-red-500">Funcionário Transportadora</span>
        <span class="text-yellow-500">Cliente</span>
        <span>John Doe</span>
        <Avatar icon="pi pi-user" />
        <Button icon="pi pi-sign-in" label="Login" class="p-button-sm" />
        <Button icon="pi pi-power-off" label="Logout" class="p-button-sm" />
        <span
          :class="isDark ? 'pi pi-sun' : 'pi pi-moon'"
          style="font-size: 1.5rem; cursor: pointer"
          @click="toggleDarkMode"
        ></span>
      </div>
    </template>
  </Menubar>
</template>

