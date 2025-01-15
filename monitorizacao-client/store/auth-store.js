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

  const token = ref(localStorage.getItem('token'));
  const user = ref(JSON.parse(localStorage.getItem('user')));

  const isAuthenticated = computed(() => !!token.value);

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
      localStorage.setItem('token', tokenText);
      await fetchUser();
      localStorage.setItem('user', JSON.stringify(user.value));
    } catch (error) {
      console.error("Login failed:", error);
      throw error;
    }
    console.log("Logged in");
    console.log(user.value);
  }

  async function fetchUser() {
    try {
      const response = await fetch(`${baseURL}/auth/user`, {
        headers: {
          'Authorization': `Bearer ${token.value}`,
        },
      });
      if (!response.ok) {
        throw new Error("Fetching user failed");
      }
      user.value = await response.json();
      localStorage.setItem('user', JSON.stringify(user.value));
    } catch (error) {
      console.error("Fetching user failed:", error);
      throw error;
    }
  }

  function logout() {
    token.value = null;
    user.value = null;
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    toast.add({ severity: 'info', summary: 'Info', detail: 'You have successfully logged out', life: 3000 });
    router.push("/");
  }

  return { token, user, isAuthenticated, login, fetchUser, logout };
});
