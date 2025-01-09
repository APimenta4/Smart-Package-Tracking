<script setup>
import { ref } from "vue";

const isOpenDialog = ref(false);
const isEditDialog = ref(false);

const deliveryCode = ref("");
const clientCode = ref("");

const newVolume = ref({
  code: "",
  packageType: "",
  products: [],
  sensors: [],
});

const editVolume = ref({
  code: "",
  packageType: "",
  products: [],
  sensors: [],
});

const volumes = ref([
  {
    code: 132,
    packageType: "FRAGILE",
    products: [
      {
        code: 550,
        quantity: 1,
      },
      {
        code: 550,
        quantity: 1,
      },
      {
        code: 550,
        quantity: 1,
      },
      {
        code: 550,
        quantity: 1,
      },
      {
        code: 550,
        quantity: 1,
      },
      {
        code: 575,
        quantity: 4,
      },
    ],
    sensors: [
      {
        code: 23,
        type: "TEMPERATURE",
      },
      {
        code: 11,
        type: "SPEED",
      },
    ],
  },
  {
    code: 13,
    packageType: "FRAGILE",
    products: [
      {
        code: 550,
        quantity: 1,
      },
      {
        code: 575,
        quantity: 4,
      },
    ],
    sensors: [
      {
        code: 23,
        type: "TEMPERATURE",
      },
      {
        code: 11,
        type: "SPEED",
      },
    ],
  },
]);

const addVolume = () => {
  volumes.value.push({ ...newVolume.value });
  newVolume.value = { code: "", packageType: "", products: [], sensors: [] };
  isOpenDialog.value = false;
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
  isEditDialog.value = true;
};

const addProductToEditVolume = () => {
  editVolume.value.products.push({ code: "", quantity: 1 });
};

const addSensorToEditVolume = () => {
  editVolume.value.sensors.push({ code: "", type: "" });
};

const saveEditedVolume = () => {
  const index = volumes.value.findIndex(
    (v) => v.code === editVolume.value.code
  );
  if (index !== -1) {
    volumes.value[index] = { ...editVolume.value };
  }
  isEditDialog.value = false;
};

const resetNewVolume = () => {
  newVolume.value = {
    code: "",
    packageType: "",
    products: [],
    sensors: [],
  };
};

const resetEditVolume = () => {
  editVolume.value = {
    code: "",
    packageType: "",
    products: [],
    sensors: [],
  };
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

const isDetailsDialogVisible = ref(false);
const selectedDetails = ref({
  title: '',
  items: []
});

const showProductDetails = (products) => {
  selectedDetails.value = {
    title: 'Products Details',
    items: products.map(p => ({
      code: p.code,
      description: `Quantity: ${p.quantity}`
    }))
  };
  isDetailsDialogVisible.value = true;
};

const showSensorDetails = (sensors) => {
  selectedDetails.value = {
    title: 'Sensors Details',
    items: sensors.map(s => ({
      code: s.code,
      description: `Type: ${s.type}`
    }))
  };
  isDetailsDialogVisible.value = true;
};
</script>
<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow mb-12">
    <Card class="mt-10">
      <template #title>
        <div class="flex justify-between items-center mb-2">
          <h2 class="text-2xl font-bold">New Delivery</h2>
        </div>
      </template>
      <template #content>
        <!-- Delivery Details Form -->
        <div class="flex gap-4 mb-6">
          <div class="flex-1">
            <span class="p-float-label">
              <label for="deliveryCode">Delivery Code</label>
              <InputText
                id="deliveryCode"
                v-model="deliveryCode"
                class="w-full"
              />
            </span>
          </div>
          <div class="flex-1">
            <span class="p-float-label">
              <label for="clientCode">Client Code</label>
              <InputText id="clientCode" v-model="clientCode" class="w-full" />
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
            <Column field="packageType" header="Package Type" />
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
          <Button label="Create Delivery" severity="success" />
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
          <InputText id="volumeCode" v-model="newVolume.code" class="w-full" />
        </span>

        <span class="p-float-label">
          <label for="packageType">Package Type</label>
          <Dropdown
            id="packageType"
            v-model="newVolume.packageType"
            :options="['FRAGILE', 'REGULAR']"
            class="w-full"
          />
        </span>

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
            />
            <Dropdown
              v-model="sensor.type"
              :options="['TEMPERATURE', 'SPEED', 'GPS']"
              placeholder="Type"
              class="w-1/2"
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
          />
        </span>

        <span class="p-float-label">
          <label for="editPackageType">Package Type</label>
          <Dropdown
            id="editPackageType"
            v-model="editVolume.packageType"
            :options="['FRAGILE', 'REGULAR']"
            class="w-full"
          />
        </span>

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
            />
            <Dropdown
              v-model="sensor.type"
              :options="['TEMPERATURE', 'SPEED', 'GPS']"
              placeholder="Type"
              class="w-1/2"
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
