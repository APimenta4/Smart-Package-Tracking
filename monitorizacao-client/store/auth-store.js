import { defineStore } from "pinia";
import { ref, computed } from "vue";
import { useRuntimeConfig } from "#app";
import { useRouter } from "vue-router";
import { useToast } from "primevue/usetoast";

export const useAuthStore = defineStore("authStore", () => {
  const config = useRuntimeConfig();
  const baseURL = config.public.API_URL;
  const router = useRouter();
  const toast = useToast();

  const token = ref(localStorage.getItem("token"));
  const user = ref(JSON.parse(localStorage.getItem("user")));
  const refreshTokenTimer = ref(null);

  const isAuthenticated = computed(() => !!token.value);

  async function refreshToken() {
    try {
      const response = await fetch(`${baseURL}/auth/refresh`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token.value}`,
        },
      });
      if (!response.ok) {
        throw new Error("Token refresh failed");
      }
      const newToken = await response.text();
      token.value = newToken;
      localStorage.setItem("token", newToken);
      startRefreshTimer();
    } catch (error) {
      console.error("Token refresh failed:", error);
      logout();
    }
  }

  function startRefreshTimer() {
    if (refreshTokenTimer.value) {
      clearTimeout(refreshTokenTimer.value);
    }
    // Set timer for 50 minutes (3000000 milliseconds)
    refreshTokenTimer.value = setTimeout(refreshToken, 3000000);
  }

  async function login(credentials) {
    try {
      const response = await fetch(`${baseURL}/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(credentials),
      });
      if (!response.ok) {
        throw new Error("Login failed");
      }
      const tokenText = await response.text();
      token.value = tokenText;
      localStorage.setItem("token", tokenText);
      await fetchUser();
      localStorage.setItem("user", JSON.stringify(user.value));
      startRefreshTimer(); 
    } catch (error) {
      console.error("Login failed:", error);
      throw error;
    }
  }

  async function fetchUser() {
    try {
      const response = await fetch(`${baseURL}/auth/user`, {
        headers: {
          Authorization: `Bearer ${token.value}`,
        },
      });
      if (!response.ok) {
        throw new Error("Fetching user failed");
      }
      user.value = await response.json();
      localStorage.setItem("user", JSON.stringify(user.value));
    } catch (error) {
      console.error("Fetching user failed:", error);
      throw error;
    }
  }

  function logout() {
    if (refreshTokenTimer.value) {
      clearTimeout(refreshTokenTimer.value);
      refreshTokenTimer.value = null;
    }
    token.value = null;
    user.value = null;
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    toast.add({
      severity: "info",
      summary: "Info",
      detail: "You have successfully logged out",
      life: 3000,
    });
    router.push("/");
  }

  function getUserRole() {
    if (isAuthenticated.value) {
      return user.value.role;
    }
    return null;
  }

  // Start refresh timer if there's an existing token
  if (token.value) {
    startRefreshTimer();
  }

  return {
    token,
    user,
    isAuthenticated,
    login,
    fetchUser,
    logout,
    getUserRole,
    refreshToken,
  };
});