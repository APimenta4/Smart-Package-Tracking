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

const editExistingVolume = (volume) => {
  editVolume.value = { ...volume };
  isEditDialog.value = true;
};

const saveEditedVolume = () => {
  const index = volumes.value.findIndex(v => v.code === editVolume.value.code);
  if (index !== -1) {
    volumes.value[index] = { ...editVolume.value };
  }
  isEditDialog.value = false;
};
</script>
<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow mb-12">
    <Card class="mt-10">
      <template #title>
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-bold">New Delivery</h2>
        </div>
      </template>
      <template #content>
        <!-- Delivery Details Form -->
        <div class="flex gap-4 mb-6">
          <div class="flex-1">
            <span class="p-float-label">
              <label for="deliveryCode">Delivery Code</label>
              <InputText id="deliveryCode" v-model="deliveryCode" class="w-full" />
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
            <Column header="Products" >
              <template #body="{ data }">
                {{ data.products.length }} products
              </template>
            </Column>
            <Column header="Sensors">
              <template #body="{ data }">
                {{ data.sensors.length }} sensors
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
      :style="{ width: '50vw' }"
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
        <div class="border p-4 rounded">
          <div class="flex justify-between items-center mb-4">
            <h4 class="font-semibold">Products</h4>
            <Button 
              icon="pi pi-plus" 
              text 
              @click="addProductToVolume"
            />
          </div>
          <div v-for="(product, index) in newVolume.products" :key="index" class="flex gap-2 mb-2">
            <InputText v-model="product.code" placeholder="Product Code" class="flex-1" />
            <InputNumber v-model="product.quantity" placeholder="Qty" :min="1" class="flex-1" />
          </div>
        </div>

        <!-- Sensors Section -->
        <div class="border p-4 rounded">
          <div class="flex justify-between items-center mb-4">
            <h4 class="font-semibold">Sensors</h4>
            <Button 
              icon="pi pi-plus" 
              text 
              @click="addSensorToVolume"
            />
          </div>
          <div v-for="(sensor, index) in newVolume.sensors" :key="index" class="flex gap-2 mb-2">
            <InputText v-model="sensor.code" placeholder="Sensor Code" class="flex-1" />
            <Dropdown 
              v-model="sensor.type" 
              :options="['TEMPERATURE', 'SPEED', 'GPS']" 
              placeholder="Type"
              class="flex-1"
            />
          </div>
        </div>
      </div>

      <template #footer>
        <Button label="Cancel" text @click="isOpenDialog = false" />
        <Button label="Add" @click="addVolume" />
      </template>
    </Dialog>

    <!-- Edit Volume Dialog -->
    <Dialog 
      v-model:visible="isEditDialog" 
      modal 
      header="Edit Volume"
      :style="{ width: '50vw' }"
    >
      <!-- Similar form as Add Volume Dialog but with editVolume.value -->
      <div class="flex flex-col gap-4">
        <span class="p-float-label">
          <InputText id="editVolumeCode" v-model="editVolume.code" class="w-full" disabled />
          <label for="editVolumeCode">Volume Code</label>
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
        <div class="border p-4 rounded">
          <div class="flex justify-between items-center mb-4">
            <h4 class="font-semibold">Products</h4>
          </div>
          <div v-for="(product, index) in editVolume.products" :key="index" class="flex gap-2 mb-2">
            <InputText v-model="product.code" placeholder="Product Code" class="flex-1" />
            <InputNumber v-model="product.quantity" placeholder="Qty" :min="1" class="w-24" />
          </div>
        </div>

        <!-- Sensors Section -->
        <div class="border p-4 rounded">
          <div class="flex justify-between items-center mb-4">
            <h4 class="font-semibold">Sensors</h4>
          </div>
            <div v-for="(sensor, index) in editVolume.sensors" :key="index" class="flex gap-2 mb-2">
            <InputText v-model="sensor.code" placeholder="Sensor Code" class="flex-1" />
            <Dropdown 
              v-model="sensor.type" 
              :options="['TEMPERATURE', 'SPEED', 'GPS']" 
              placeholder="Type"
              class="w-60"
            />
            </div>
        </div>
      </div>

      <template #footer>
        <Button label="Cancel" text @click="isEditDialog = false" />
        <Button label="Save" @click="saveEditedVolume" />
      </template>
    </Dialog>
  </div>
</template>