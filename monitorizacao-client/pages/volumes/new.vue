<script setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "../store/auth-store";
import { useToast } from "primevue/usetoast"; 

const authStore = useAuthStore();
const toast = useToast();

const config = useRuntimeConfig();
const api = config.public.API_URL;

const volumeCode = ref("");
const orderCode = ref("");
const packageType = ref("NONE");
const products = ref([]);
const sensors = ref([]);

const router = useRouter();

if (!authStore.isAuthenticated || authStore.user.role !== "Logistician") {
  router.push("/");
}

const validateString = (value) => typeof value === 'string' && value.trim().length > 0;

const isFormValid = computed(() => validateString(volumeCode.value) && validateString(orderCode.value) && products.value.every(p => validateString(p.code)) && sensors.value.every(s => validateString(s.code) && validateString(s.type)));

const addProduct = () => {
  products.value.push({ code: "", quantity: 1 });
};

const addSensor = () => {
  sensors.value.push({ code: "", type: "" });
};

const removeProduct = (index) => {
  products.value.splice(index, 1);
};

const removeSensor = (index) => {
  sensors.value.splice(index, 1);
};

async function createVolume() {
  if (
    !validateString(volumeCode.value) ||
    !validateString(orderCode.value) ||
    !products.value.every(p => validateString(p.code)) ||
    !sensors.value.every(s => validateString(s.code) && validateString(s.type))
  ) {
    console.error("Volume Code, Order Code, Products, and Sensors are required.");
    toast.add({ severity: 'error', summary: 'Error', detail: 'Volume Code, Order Code, Products, and Sensors are required.', life: 3000 });
    return;
  }
  try {
    const response = await fetch(`${api}/volumes`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        'Authorization': `Bearer ${authStore.token}`
      },
      body: JSON.stringify({
        code: volumeCode.value,
        orderCode: orderCode.value,
        packageType: packageType.value,
        products: products.value,
        sensors: sensors.value,
      }),
    });
    if (!response.ok) {
      const errorText = await response.text();
      let errorMessage = "Failed to create new volume";
      try {
        const errorData = JSON.parse(errorText);
        errorMessage = errorData.message || errorMessage;
      } catch (e) {
        errorMessage = errorText;
      }
      throw new Error(errorMessage);
    }
    toast.add({ severity: 'success', summary: 'Success', detail: 'Volume created successfully', life: 3000 }); 
    router.push("/");
  } catch (error) {
    console.error("Failed to create new volume:", error);
    toast.add({ severity: 'error', summary: 'Error', detail: error.message, life: 3000 }); 
  }
}

const packageTypeOptions = [
  { label: 'Fragile', value: 'FRAGILE' },
  { label: 'Isotermic', value: 'ISOTERMIC' },
  { label: 'Geolocation', value: 'GEOLOCATION' }
];

const selectedPackageTypes = ref([]);

const combinePackageTypes = (types) => {
  if (!types || types.length === 0) return 'NONE';
  
  const orderMap = {
    FRAGILE: 1,
    ISOTERMIC: 2,
    GEOLOCATION: 3
  };
  
  return types
    .sort((a, b) => orderMap[a] - orderMap[b])
    .join('_');
};

watch(selectedPackageTypes, (newTypes) => {
  packageType.value = combinePackageTypes(newTypes);
});
</script>

<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow mb-12">
    <Card class="mt-10">
      <template #title>
        <div class="flex justify-between items-center mb-2">
          <h2 class="text-2xl font-bold">New Volume</h2>
        </div>
      </template>
      <template #content>
        <!-- Volume Details Form -->
        <div class="flex gap-4 mb-6">
          <div class="flex-1">
            <span class="p-float-label">
              <label for="volumeCode">Volume Code</label>
              <InputText
                id="volumeCode"
                v-model="volumeCode"
                class="w-full"
                :class="{ 'p-invalid': !validateString(volumeCode) }"
              />
              <small v-if="!validateString(volumeCode)" class="p-error">Volume Code is required.</small>
            </span>
          </div>
          <div class="flex-1">
            <span class="p-float-label">
              <label for="orderCode">Delivery Code</label>
              <InputText
                id="orderCode"
                v-model="orderCode"
                class="w-full"
                :class="{ 'p-invalid': !validateString(orderCode) }"
              />
              <small v-if="!validateString(orderCode)" class="p-error">Delivery Code is required.</small>
            </span>
          </div>
        </div>

        <div class="flex flex-col gap-2 mb-6">
          <label class="font-semibold">Package Types (Optional)</label>
          <div class="flex justify-center">
            <SelectButton
              v-model="selectedPackageTypes"
              :options="packageTypeOptions"
              :multiple="true"
              optionLabel="label"
              optionValue="value"
              class="flex-wrap"
            />
          </div>
        </div>

        <!-- Products Section -->
        <div class="p-4 rounded mb-6">
          <div class="flex justify-between items-center mb-4">
            <h4 class="font-semibold">Products</h4>
            <Button
              icon="pi pi-plus"
              text
              label="Add Product"
              @click="addProduct"
            />
          </div>
          <div class="flex gap-2 mb-2 font-semibold text-sm">
            <span class="w-1/2 ml-1">Code</span>
            <span class="w-1/2">Quantity</span>
          </div>
          <div
            v-for="(product, index) in products"
            :key="index"
            class="flex gap-2 mb-2"
          >
            <InputText
              v-model="product.code"
              placeholder="Product Code"
              class="w-1/2"
              :class="{ 'p-invalid': !validateString(product.code) }"
            />
            <InputNumber
              v-model="product.quantity"
              placeholder="Qty"
              :min="1"
              :max="999"
              class="w-1/2"
              showButtons
              decrementButtonClass="p-button-secondary"
              incrementButtonClass="p-button-secondary"
              incrementButtonIcon="pi pi-plus"
              decrementButtonIcon="pi pi-minus"
              suffix=" units"
            />
            <Button
              icon="pi pi-trash"
              text
              rounded
              severity="danger"
              @click="removeProduct(index)"
            />
          </div>
        </div>

        <!-- Sensors Section -->
        <div class="p-4 rounded mb-6">
          <div class="flex justify-between items-center mb-4">
            <h4 class="font-semibold">Sensors</h4>
            <Button
              icon="pi pi-plus"
              label="Add Sensor"
              text
              @click="addSensor"
            />
          </div>
          <div class="flex gap-2 mb-2 font-semibold text-sm">
            <span class="w-1/2 ml-1">Code</span>
            <span class="w-1/2">Type</span>
          </div>
          <div
            v-for="(sensor, index) in sensors"
            :key="index"
            class="flex gap-2 mb-2"
          >
            <InputText
              v-model="sensor.code"
              placeholder="Sensor Code"
              class="w-1/2"
              :class="{ 'p-invalid': !validateString(sensor.code) }"
            />
            <Dropdown
              v-model="sensor.type"
              :options="['ACCELERATION', 'TEMPERATURE', 'LOCATION']"
              placeholder="Type"
              class="w-1/2"
              :class="{ 'p-invalid': !validateString(sensor.type) }"
            />
            <Button
              icon="pi pi-trash"
              text
              rounded
              severity="danger"
              @click="removeSensor(index)"
            />
          </div>
        </div>

        <!-- Submit Button -->
        <div class="flex justify-end">
          <Button
            label="Create Volume"
            severity="success"
            :disabled="!isFormValid"
            @click.prevent="createVolume"
          />
        </div>
      </template>
    </Card>
  </div>
</template>

<style scoped>
:deep(.p-selectbutton) {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 0.5rem;
}

:deep(.p-selectbutton .p-button) {
  margin: 0;
}
</style>
