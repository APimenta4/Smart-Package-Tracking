<script setup>
import { ref, onMounted } from "vue";

const isDark = ref(false);

function toggleDarkMode() {
  isDark.value = !isDark.value;
  document.documentElement.classList.toggle("my-app-dark", isDark.value);
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
    document.documentElement.classList.add("my-app-dark");
  }
});
</script>
<template>
  <Menubar :model="items" class="m-5 rounded-none"/>
  <div>
    <Button label="Click Me" />
    <InputText placeholder="Enter text here" />
    <span
      :class="isDark ? 'pi pi-sun' : 'pi pi-moon'"
      style="font-size: 1.5rem; cursor: pointer"
      @click="toggleDarkMode"
    ></span>
  </div>
</template>
