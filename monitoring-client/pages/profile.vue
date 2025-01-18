<script setup>
import { ref } from "vue";
import { useAuthStore } from "../store/auth-store";
import { useRouter } from "vue-router";
import { useToast } from "primevue/usetoast";

const auth = useAuthStore();
const router = useRouter();
const toast = useToast();

if (!auth.isAuthenticated) {
  router.push("/");
}

const user = ref({
  name: auth.user?.name || "John Doe",
  email: auth.user?.email || "johndoe@example.com",
});

const showChangePasswordDialog = ref(false);
const passwordForm = ref({
  oldPassword: "",
  newPassword: "",
  confirmPassword: "",
});
const loading = ref(false);

const toggleChangePasswordDialog = () => {
  showChangePasswordDialog.value = !showChangePasswordDialog.value;
  if (!showChangePasswordDialog.value) {
    clearPasswordForm();
  }
};

const clearPasswordForm = () => {
  passwordForm.value.oldPassword = "";
  passwordForm.value.newPassword = "";
  passwordForm.value.confirmPassword = "";
};

const handleChangePassword = async () => {
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    toast.add({
      severity: "error",
      summary: "Error",
      detail: "Confirm password does not match new password.",
      life: 3000,
    });
    return;
  }

  loading.value = true;
  try {
    await auth.changePassword(passwordForm.value.oldPassword, passwordForm.value.newPassword, passwordForm.value.newPassword);
    toast.add({
      severity: "success",
      summary: "Success",
      detail: "Password changed successfully.",
      life: 3000,
    });
    clearPasswordForm();
    showChangePasswordDialog.value = false;
  } catch (error) {
    console.error("Password change failed:", error);
    toast.add({
      severity: "error",
      summary: "Error",
      detail: error.message,
      life: 3000,
    });
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow mb-12">
    <Card class="mt-10 w-1/2 mx-auto min-w-[270px]">
      <template #title>
        <h2 class="text-2xl font-bold">Profile</h2>
      </template>
      <template #content>
        <!-- User Information -->
        <div class="mb-6">
          <div class="flex flex-col gap-2">
            <div class="flex items-center">
              <span class="font-medium w-20">Name:</span>
              <span>{{ user?.name }}</span>
            </div>
            <div class="flex items-center">
              <span class="font-medium w-20">Email:</span>
              <span>{{ user?.email }}</span>
            </div>
          </div>
        </div>

        <!-- Change Password Button -->
        <div>
          <Button
            label="Change Password"
            @click="toggleChangePasswordDialog"
            severity="warning"
          />
        </div>
      </template>
    </Card>

    <!-- Change Password Dialog -->
    <Dialog
      v-model:visible="showChangePasswordDialog"
      modal
      :header="'Change Password'"
      :style="{ width: '90vw', maxWidth: '30rem' }"
    >
      <template #header>
        <div class="flex items-center gap-2">
          <i class="pi pi-key text-xl"></i>
          <span class="text-xl font-bold">Change Password</span>
        </div>
      </template>
      <div class="flex flex-col gap-4">
        <span class="p-float-label">
          <label for="old-password">Old Password</label>
          <Password
            id="old-password"
            v-model="passwordForm.oldPassword"
            :feedback="false"
            toggleMask
            class="!w-full"
            :inputStyle="{ width: '100%' }"
            required
          />
        </span>
        <span class="p-float-label">
          <label for="new-password">New Password</label>
          <Password
            id="new-password"
            v-model="passwordForm.newPassword"
            :feedback="false"
            toggleMask
            class="!w-full"
            :inputStyle="{ width: '100%' }"
            required
          />
        </span>
        <span class="p-float-label">
          <label for="confirm-password">Confirm Password</label>
          <Password
            id="confirm-password"
            v-model="passwordForm.confirmPassword"
            :feedback="false"
            toggleMask
            class="!w-full"
            :inputStyle="{ width: '100%' }"
            required
          />
        </span>
      </div>

      <template #footer>
        <Button label="Cancel" text @click="toggleChangePasswordDialog" />
        <Button
          label="Save Password"
          @click="handleChangePassword"
          :loading="loading"
          severity="warning"
        />
      </template>
    </Dialog>
  </div>
</template>
