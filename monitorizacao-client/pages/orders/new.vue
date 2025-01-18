<script setup>
import { ref, watch, computed } from "vue";
import { useToast } from "primevue/usetoast";
import { useAuthStore } from "../store/auth-store";

const authStore = useAuthStore();
const toast = useToast(); 

const config = useRuntimeConfig();
const api = config.public.API_URL;

const isOpenDialog = ref(false);
const isEditDialog = ref(false);

const deliveryCode = ref("");
const clientCode = ref("");

const router = useRouter();

if (!authStore.isAuthenticated || authStore.user.role !== "Logistician") {
  router.push("/");
}

const newVolume = ref({
  code: "",
  packageType: "NONE",
  products: [],
  sensors: [],
});

const editVolume = ref({
  code: "",
  packageType: "NONE",
  products: [],
  sensors: [],
});


const volumes = ref([]);

const validateString = (value) => typeof value === 'string' && value.trim().length > 0;

const isFormValid = computed(() => validateString(deliveryCode.value) && validateString(clientCode.value));

const isVolumeValid = computed(() => validateString(newVolume.value.code) && newVolume.value.products.every(p => validateString(p.code)) && newVolume.value.sensors.every(s => validateString(s.code) && validateString(s.type)));

const isEditVolumeValid = computed(() => validateString(editVolume.value.code) && editVolume.value.products.every(p => validateString(p.code)) && editVolume.value.sensors.every(s => validateString(s.code) && validateString(s.type)));

const addVolume = () => {
  if (isVolumeValid.value) {
    volumes.value.push({ ...newVolume.value });
    resetNewVolume();
    isOpenDialog.value = false;
  } else {
    console.error("Invalid volume data");
  }
};

const removeVolume = (volume) => {
  const index = volumes.value.indexOf(volume);
  volumes.value.splice(index, 1);
};

const addProductToVolume = () => {
  newVolume.value.products.push({ code: "", quantity: 1 });
};

const addSensorToVolume = () => {
  newVolume.value.sensors.push({ code: "", type: "" });
};

const originalVolume = ref(null);

const editExistingVolume = (volume) => {
  originalVolume.value = JSON.parse(JSON.stringify(volume));
  editVolume.value = { ...volume };
  editSelectedPackageTypes.value = parsePackageType(volume.packageType);
  isEditDialog.value = true;
};

const addProductToEditVolume = () => {
  editVolume.value.products.push({ code: "", quantity: 1 });
};

const addSensorToEditVolume = () => {
  editVolume.value.sensors.push({ code: "", type: "" });
};

const saveEditedVolume = () => {
  if (isEditVolumeValid.value) {
    const index = volumes.value.findIndex(
      (v) => v.code === originalVolume.value.code
    );
    if (index !== -1) {
      volumes.value[index] = { ...editVolume.value };
    }
    isEditDialog.value = false;
  } else {
    console.error("Invalid edited volume data");
  }
  originalVolume.value = null; 
};

const resetNewVolume = () => {
  newVolume.value = {
    code: "",
    packageType: "NONE",
    products: [],
    sensors: [],
  };
  selectedPackageTypes.value = [];
};

const resetEditVolume = () => {
  editVolume.value = {
    code: "",
    packageType: "NONE",
    products: [],
    sensors: [],
  };
  editSelectedPackageTypes.value = [];
};

const handleNewDialogCancel = () => {
  resetNewVolume();
  isOpenDialog.value = false;
};

const handleEditDialogCancel = () => {
  const index = volumes.value.findIndex(
    (v) => v.code === originalVolume.value.code
  );
  if (index !== -1) {
    volumes.value[index] = originalVolume.value;
  }
  resetEditVolume();
  originalVolume.value = null;
  isEditDialog.value = false;
};

const removeProductFromVolume = (index) => {
  newVolume.value.products.splice(index, 1);
};

const removeSensorFromVolume = (index) => {
  newVolume.value.sensors.splice(index, 1);
};

const removeProductFromEditVolume = (index) => {
  editVolume.value.products.splice(index, 1);
};

const removeSensorFromEditVolume = (index) => {
  editVolume.value.sensors.splice(index, 1);
};

const isDetailsDialogVisible = ref(false);
const selectedDetails = ref({
  title: "",
  items: [],
});

const showProductDetails = (products) => {
  selectedDetails.value = {
    title: "Products Details",
    items: products.map((p) => ({
      code: p.code,
      description: `Quantity: ${p.quantity}`,
    })),
  };
  isDetailsDialogVisible.value = true;
};

const showSensorDetails = (sensors) => {
  selectedDetails.value = {
    title: "Sensors Details",
    items: sensors.map((s) => ({
      code: s.code,
      description: `Type: ${s.type}`,
    })),
  };
  isDetailsDialogVisible.value = true;
};

async function createDelivery() {
  try {
    const response = await fetch(`${api}/orders`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        'Authorization': `Bearer ${authStore.token}`
      },
      body: JSON.stringify({
        clientCode: clientCode.value,
        code: deliveryCode.value,
        volumes: volumes.value,
      }),
    });
    if (!response.ok) {
      const errorText = await response.text();
      let errorMessage = "Failed to create new order";
      try {
        const errorData = JSON.parse(errorText);
        errorMessage = errorData.message || errorMessage;
      } catch (e) {
        errorMessage = errorText;
        errorMessage = errorMessage.replace(/\./g, ".\n")
      }
      throw new Error(errorMessage);
    }
    toast.add({ severity: 'success', summary: 'Success', detail: 'Order created successfully', life: 3000 }); 
    router.push("/");
  } catch (error) {
    console.error("Failed to create new order:", error);
    toast.add({ severity: 'error', summary: 'Error', detail: error.message, life: 3000 }); 
  }
}

const packageTypeOptions = [
  { label: 'Fragile', value: 'FRAGILE' },
  { label: 'Isotermic', value: 'ISOTERMIC' },
  { label: 'Geolocation', value: 'GEOLOCATION' }
];

const selectedPackageTypes = ref([]);
const editSelectedPackageTypes = ref([]);

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

const parsePackageType = (packageType) => {
  if (!packageType || packageType === 'NONE') return [];
  return packageType.split('_');
};

watch(selectedPackageTypes, (newTypes) => {
  newVolume.value.packageType = combinePackageTypes(newTypes);
});

watch(editSelectedPackageTypes, (newTypes) => {
  editVolume.value.packageType = combinePackageTypes(newTypes);
});

const getPackageTypeSeverity = (type) => {
  const severityMap = {
    FRAGILE: "danger",
    ISOTERMIC: "warning",
    GEOLOCATION: "info",
    NONE: "secondary",
  };
  return severityMap[type] || "secondary";
};
</script>
<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow mb-12">
    <Card class="mt-10">
      <template #title>
        <div class="flex justify-between items-center mb-2">
          <h2 class="text-2xl font-bold">New Order</h2>
        </div>
      </template>
      <template #content>
        <!-- Delivery Details Form -->
        <div class="flex gap-4 mb-6">
          <div class="flex-1">
            <span class="p-float-label">
              <label for="deliveryCode">Order Code</label>
              <InputText
                id="deliveryCode"
                v-model="deliveryCode"
                class="w-full"
                :class="{ 'p-invalid': !validateString(deliveryCode) }"
              />
              <small v-if="!validateString(deliveryCode)" class="p-error">Order Code is required.</small>
            </span>
          </div>
          <div class="flex-1">
            <span class="p-float-label">
              <label for="clientCode">Client Code</label>
              <InputText id="clientCode" v-model="clientCode" class="w-full" :class="{ 'p-invalid': !validateString(clientCode) }" />
              <small v-if="!validateString(clientCode)" class="p-error">Client Code is required.</small>
            </span>
          </div>
        </div>

        <!-- Volumes Section -->
        <div class="mb-4">
          <div class="flex justify-between items-center mb-4">
            <h3 class="text-xl font-semibold">Volumes</h3>
            <Button
              label="Add Volume"
              icon="pi pi-plus"
              @click="isOpenDialog = true"
            />
          </div>

          <!-- Volumes Table -->
          <DataTable :value="volumes" class="mb-4">
            <Column field="code" header="Code" />
            <Column field="packageType" header="Package Type">
              <template #body="{ data }">
                <div class="flex flex-wrap gap-1">
                  <template v-for="type in data.packageType.split('_')" :key="type">
                    <Tag :value="type" :severity="getPackageTypeSeverity(type)" />
                  </template>
                </div>
              </template>
            </Column>
            <Column header="Products">
              <template #body="{ data }">
                <Button
                  link
                  class="text-blue-600 hover:text-blue-800 p-0"
                  @click="showProductDetails(data.products)"
                >
                  {{ data.products.length }} products
                </Button>
              </template>
            </Column>
            <Column header="Sensors">
              <template #body="{ data }">
                <Button
                  link
                  class="text-blue-600 hover:text-blue-800 p-0"
                  @click="showSensorDetails(data.sensors)"
                >
                  {{ data.sensors.length }} sensors
                </Button>
              </template>
            </Column>
            <Column header="Actions">
              <template #body="{ data }">
                <div class="flex gap-2">
                  <Button
                    icon="pi pi-pencil"
                    text
                    rounded
                    severity="success"
                    @click="editExistingVolume(data)"
                  />
                  <Button
                    icon="pi pi-trash"
                    text
                    rounded
                    severity="danger"
                    @click="removeVolume(data)"
                  />
                </div>
              </template>
            </Column>
          </DataTable>
        </div>

        <!-- Submit Button -->
        <div class="flex justify-end">
          <Button
            label="Create Order"
            severity="success"
            :disabled="!isFormValid"
            @click.prevent="createDelivery"
          />
        </div>
      </template>
    </Card>

    <!-- Add Volume Dialog -->
    <Dialog
      v-model:visible="isOpenDialog"
      modal
      header="Add New Volume"
      :style="{ width: '35vw', minWidth: '350px' }"
    >
      <div class="flex flex-col gap-4">
        <span class="p-float-label">
          <label for="volumeCode">Volume Code</label>
          <InputText id="volumeCode" v-model="newVolume.code" class="w-full" :class="{ 'p-invalid': !validateString(newVolume.code) }" />
          <small v-if="!validateString(newVolume.code)" class="p-error">Volume Code is required.</small>
        </span>

        <div class="flex flex-col gap-2">
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
        <div class="p-4 rounded">
          <div class="flex justify-between items-center mb-4">
            <h4 class="font-semibold">Products</h4>
            <Button
              icon="pi pi-plus"
              text
              label="Add Product"
              @click="addProductToVolume"
            />
          </div>
          <div class="flex gap-2 mb-2 font-semibold text-sm">
            <span class="w-1/2 ml-1">Code</span>
            <span class="w-1/2 ml-4">Quantity</span>
          </div>
          <div
            v-for="(product, index) in newVolume.products"
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
              @click="removeProductFromVolume(index)"
            />
          </div>
        </div>

        <!-- Sensors Section -->
        <div class="p-4 rounded">
          <div class="flex justify-between items-center mb-4">
            <h4 class="font-semibold">Sensors</h4>
            <Button
              icon="pi pi-plus"
              label="Add Sensor"
              text
              @click="addSensorToVolume"
            />
          </div>
          <div class="flex gap-2 mb-2 font-semibold text-sm">
            <span class="w-1/2 ml-1">Code</span>
            <span class="w-1/2 ml-5">Type</span>
          </div>
          <div
            v-for="(sensor, index) in newVolume.sensors"
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
              @click="removeSensorFromVolume(index)"
            />
          </div>
        </div>
      </div>

      <template #footer>
        <Button label="Cancel" text @click="handleNewDialogCancel" />
        <Button label="Add" @click="addVolume" />
      </template>
    </Dialog>

    <!-- Edit Volume Dialog -->
    <Dialog
      v-model:visible="isEditDialog"
      modal
      header="Edit Volume"
      :style="{ width: '35vw', minWidth: '350px' }"
    >
      <!-- Similar form as Add Volume Dialog but with editVolume.value -->
      <div class="flex flex-col gap-4">
        <span class="p-float-label">
          <label for="editVolumeCode">Volume Code</label>
          <InputText
            id="editVolumeCode"
            v-model="editVolume.code"
            class="w-full"
            :class="{ 'p-invalid': !validateString(editVolume.code) }"
          />
          <small v-if="!validateString(editVolume.code)" class="p-error">Volume Code is required.</small>
        </span>

        <div class="flex flex-col gap-2">
          <label class="font-semibold">Package Types</label>
          <div class="flex justify-center">
            <SelectButton
              v-model="editSelectedPackageTypes"
              :options="packageTypeOptions"
              :multiple="true"
              optionLabel="label"
              optionValue="value"
              class="flex-wrap"
            />
          </div>
        </div>

        <!-- Products Section -->

        <div class="p-4 rounded">
          <div class="flex justify-between items-center mb-4">
            <h4 class="font-semibold">Products</h4>
            <Button
              icon="pi pi-plus"
              text
              label="Add Product"
              @click="addProductToEditVolume"
            />
          </div>
          <div class="flex gap-2 mb-2 font-semibold text-sm">
            <span class="w-1/2 ml-1">Code</span>
            <span class="w-1/2 ml-4">Quantity</span>
          </div>
          <div
            v-for="(product, index) in editVolume.products"
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
              @click="removeProductFromEditVolume(index)"
            />
          </div>
        </div>

        <!-- Edit Volume Dialog Sensors Section -->
        <div class="p-4 rounded">
          <div class="flex justify-between items-center mb-4">
            <h4 class="font-semibold">Sensors</h4>
            <Button
              icon="pi pi-plus"
              label="Add Sensor"
              text
              @click="addSensorToEditVolume"
            />
          </div>
          <div class="flex gap-2 mb-2 font-semibold text-sm">
            <span class="w-1/2 ml-1">Code</span>
            <span class="w-1/2 ml-5">Type</span>
          </div>
          <div
            v-for="(sensor, index) in editVolume.sensors"
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
              @click="removeSensorFromEditVolume(index)"
            />
          </div>
        </div>
      </div>

      <template #footer>
        <Button label="Cancel" text @click="handleEditDialogCancel" />
        <Button label="Save" @click="saveEditedVolume" />
      </template>
    </Dialog>

    <Dialog
      v-model:visible="isDetailsDialogVisible"
      :header="selectedDetails.title"
      modal
      :style="{ width: '450px' }"
    >
      <DataTable :value="selectedDetails.items" :rows="5">
        <Column field="code" header="Code">
          <template #body="{ data }">
            <span>{{ data.code }}</span>
          </template>
        </Column>
        <Column field="description" header="Description" />
      </DataTable>
    </Dialog>
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