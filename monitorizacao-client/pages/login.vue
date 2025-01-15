<script setup>
import { ref } from "vue";
import { useAuthStore } from "../store/auth-store";
import { useRouter } from "vue-router";

const auth = useAuthStore();
const router = useRouter();

const credentials = ref({
  code: "",
  password: "",
});

const loading = ref(false);

const handleLogin = async () => {
  loading.value = true;
  try {
    await auth.login(credentials.value);
    router.push("/");
  } catch (error) {
    console.error("Login failed:", error);
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow items-center justify-center mb-12">
    <Card class="w-full max-w-md p-4">
      <template #title>
        <h2 class="text-2xl font-bold text-center mb-4">Login</h2>
      </template>
      <template #content>
        <form @submit.prevent="handleLogin" class="flex flex-col gap-4">
          <div class="flex flex-col gap-2">
            <span class="p-float-label">
              <label for="code">Code</label>
              <InputText
                id="code"
                v-model="credentials.code"
                class="w-full"
                required
              />
            </span>
          </div>

          <div class="flex flex-col gap-2">
            <span class="p-float-label">
              <label for="password">Password</label>
              <Password
                id="password"
                v-model="credentials.password"
                :feedback="false"
                toggleMask
                class="!w-full"
                :inputStyle="{ width: '100%' }"
                required
              />
            </span>
          </div>

          <Button
            type="submit"
            label="Login"
            :loading="loading"
            class="w-full mt-5"
          />
        </form>
      </template>
    </Card>
  </div>
</template>
