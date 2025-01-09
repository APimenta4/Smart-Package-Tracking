<script setup>
import { ref } from "vue";

const credentials = ref({
  email: "",
  password: "",
});

const loading = ref(false);

const handleLogin = async () => {
  loading.value = true;
  try {
    // TODO: Implement actual login logic here
    await new Promise((resolve) => setTimeout(resolve, 1000)); // Simulate API call
    console.log("Login attempt:", credentials.value);
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
              <label for="email">Email</label>
              <InputText
                id="emails"
                v-model="credentials.email"
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
