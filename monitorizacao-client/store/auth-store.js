import { defineStore } from "pinia";
import { ref, computed } from "vue";

export const useAuthStore = defineStore("authStore", () => {
  const token = ref(null);
  const user = ref(null);

  const isAuthenticated = computed(() => !!token.value);

  function logout() {
    token.value = null;
    user.value = null;
  }

  return { token, user, isAuthenticated, logout };
});
