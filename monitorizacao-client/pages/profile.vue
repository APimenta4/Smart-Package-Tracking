<script setup>
import { ref } from "vue";
import { useAuthStore } from "../store/auth-store";
import { useToast} from "primevue/usetoast";

const auth = useAuthStore();
const toast = useToast();

// User information to be displayed
const user = ref({
    name: auth.user?.name || "John Doe",
    email: auth.user?.email || "johndoe@example.com",
});

// Form state for changing password
const showChangePassword = ref(false);
const passwordForm = ref({
    newPassword: "",
    confirmPassword: "",
});
const loading = ref(false);

// Toggle the change password section
const toggleChangePassword = () => {
    showChangePassword.value = !showChangePassword.value;
};

// Handle password change
const handleChangePassword = async () => {
    if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
        toast.add({
            severity: "error",
            summary: "Error",
            detail: "Passwords do not match.",
            life: 3000,
        });
        return;
    }

    loading.value = true;
    try {
        // Call the store action or API for password update
        await auth.changePassword(passwordForm.value.newPassword);

        toast.add({
            severity: "success",
            summary: "Success",
            detail: "Password changed successfully.",
            life: 3000,
        });

        // Clear form and hide password section
        passwordForm.value.newPassword = "";
        passwordForm.value.confirmPassword = "";
        showChangePassword.value = false;
    } catch (error) {
        console.error("Password change failed:", error);
        toast.add({
            severity: "error",
            summary: "Error",
            detail: "Failed to change password. Please try again later.",
            life: 3000,
        });
    } finally {
        loading.value = false;
    }
};
</script>

<template>
<div class="min-h-screen bg-gray-100 p-4">
    <!-- Profile Content -->
    <div class="max-w-2xl mx-auto bg-white rounded-lg shadow p-6">
        <h1 class="text-2xl font-bold text-gray-800 mb-4">Profile</h1>

        <!-- User Information -->
        <div class="mb-6">
            <p class="text-gray-600">
                <span class="font-medium text-gray-800">Name:</span> {{ user.name }}
            </p>
            <p class="text-gray-600">
                <span class="font-medium text-gray-800">Email:</span> {{ user.email }}
            </p>
        </div>

        <!-- Change Password Button -->
        <div>
            <button @click="toggleChangePassword" class="bg-amber-500 text-white font-medium px-4 py-2 rounded hover:bg-amber-600">
                Change Password
            </button>

            <!-- Change Password Form -->
            <div v-if="showChangePassword" class="mt-4 p-4 border border-gray-200 rounded-lg bg-gray-50 shadow">
                <h2 class="text-lg font-bold text-gray-800 mb-4">Change Password</h2>
                <form @submit.prevent="handleChangePassword">
                    <!-- New Password -->
                    <div class="mb-4">
                        <label for="new-password" class="block text-gray-700 font-medium mb-2">New Password</label>
                        <input type="password" id="new-password" v-model="passwordForm.newPassword" class="w-full px-3 py-2 border rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-amber-500" required />
                    </div>

                    <!-- Confirm Password -->
                    <div class="mb-4">
                        <label for="confirm-password" class="block text-gray-700 font-medium mb-2">Confirm Password</label>
                        <input type="password" id="confirm-password" v-model="passwordForm.confirmPassword" class="w-full px-3 py-2 border rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-amber-500" required />
                    </div>

                    <!-- Save Password Button -->
                    <button type="submit" :class="{ 'opacity-50 cursor-not-allowed': loading }" :disabled="loading" class="bg-amber-500 text-white font-medium px-4 py-2 rounded hover:bg-amber-600 w-full">
                        Save Password
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>
</template>

