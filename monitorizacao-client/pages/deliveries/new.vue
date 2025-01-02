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
        <div class="grid grid-cols-3 gap-8">
          <!-- Left Column - Delivery Details -->
          <Card class="col-span-1">
            <template #title>Details</template>
            <template #content>
              <div class="flex flex-col gap-4">
                <FloatLabel variant="on">
                  <InputText v-model="deliveryCode" />
                  <label>Delivery Code</label>
                </FloatLabel>
                <FloatLabel variant="on">
                  <InputText v-model="clientCode" />
                  <label>Client Code</label>
                </FloatLabel>
                <Button
                  label="Create Delivery"
                  icon="pi pi-check"
                  severity="success"
                  raised
                />
              </div>
            </template>
          </Card>

          <!-- Right Column - Volumes -->
          <Card class="col-span-2">
            <template #title>
              <div class="flex justify-between items-center">
                <span>Volumes</span>
                <Button
                  label="Add Volume"
                  @click.prevent="isOpenDialog = true"
                  icon="pi pi-plus"
                  severity="primary"
                  raised
                />
              </div>
            </template>
            <template #content>
              <div class="grid grid-cols-2 gap-4">
                <Card v-for="volume in volumes" :key="volume.code" class="shadow-sm">
                  <template #title>
                    <div class="flex justify-between items-center">
                      <span>Volume {{ volume.code }}</span>
                      <Tag :value="volume.packageType" severity="warning" />
                    </div>
                  </template>
                  <template #subtitle>
                    <div class="flex gap-2">
                      <Button
                        icon="pi pi-pencil"
                        text
                        severity="info"
                        @click="editExistingVolume(volume)"
                        tooltip="Edit Volume"
                      />
                      <Button
                        icon="pi pi-trash"
                        text
                        severity="danger"
                        @click.prevent="removeVolume(volume)"
                        tooltip="Remove Volume"
                      />
                    </div>
                  </template>
                  <template #content>
                    <div class="grid grid-cols-2 gap-4">
                      <div>
                        <h4 class="font-semibold mb-2">Products</h4>
                        <ScrollPanel class="h-[150px]">
                          <div v-for="product in volume.products" :key="product.code" 
                               class="p-2 flex justify-between items-center border-b">
                            <span>Code: {{ product.code }}</span>
                            <Tag :value="'Qty: ' + product.quantity" severity="info" />
                          </div>
                        </ScrollPanel>
                      </div>
                      <div>
                        <h4 class="font-semibold mb-2">Sensors</h4>
                        <ScrollPanel class="h-[150px]">
                          <div v-for="sensor in volume.sensors" :key="sensor.code" 
                               class="p-2 flex justify-between items-center border-b">
                            <span>{{ sensor.code }}</span>
                            <Tag :value="sensor.type" severity="success" />
                          </div>
                        </ScrollPanel>
                      </div>
                    </div>
                  </template>
                </Card>
              </div>
            </template>
          </Card>
        </div>
      </template>
    </Card>

 
  </div>

  <Dialog
    v-model:visible="isEditDialog"
    modal
    :style="{ width: '35rem' }"
  >
    <template #header>
      <span class="text-1xl font-bold">Edit Volume</span>
    </template>
    <div class="flex flex-col gap-4 mb-4">
      <div class="flex items-center gap-4">
        <label for="editVolumeCode" class="font-semibold w-24">Code</label>
        <InputText
          id="editVolumeCode"
          class="flex-auto"
          autocomplete="off"
          v-model="editVolume.code"
        />
      </div>
      <div class="flex items-center gap-4">
        <label for="editPackageType" class="font-semibold w-24">Type</label>
        <Dropdown
          id="editPackageType"
          :options="['FRAGILE', 'NORMAL']"
          class="flex-auto"
          v-model="editVolume.packageType"
        />
      </div>
    </div>
    <div class="flex gap-4">
      <div class="flex flex-col gap-4 flex-1">
        <h3 class="font-semibold text-center">Products</h3>
        <Button
          label="Add Product"
          icon="pi pi-plus"
          size="small"
          @click="editVolume.products.push({ code: '', quantity: 1 })"
          class="w-full"
        />
        <div
          v-for="(product, index) in editVolume.products"
          :key="index"
          class="flex gap-2 w-full"
        >
          <InputText
            v-model="product.code"
            placeholder="Code"
            class="flex-1"
          />
          <InputNumber
            v-model="product.quantity"
            showButtons
            buttonLayout="vertical"
            style="width: 3rem"
            :min="0"
            :max="99"
          >
            <template #incrementbuttonicon>
              <span class="pi pi-plus" />
            </template>
            <template #decrementbuttonicon>
              <span class="pi pi-minus" />
            </template>
          </InputNumber>
        </div>
      </div>
      <div class="flex flex-col gap-4 flex-1">
        <h3 class="font-semibold text-center">Sensors</h3>
        <Button
          label="Add Sensor"
          icon="pi pi-plus"
          size="small"
          @click="editVolume.sensors.push({ code: '', type: '' })"
          class="w-full"
        />
        <div
          v-for="(sensor, index) in editVolume.sensors"
          :key="index"
          class="flex gap-2 w-full"
        >
          <InputText
            v-model="sensor.code"
            placeholder="Code"
            class="flex-1"
          />
          <InputText
            v-model="sensor.type"
            placeholder="Type"
          />
        </div>
      </div>
    </div>
    <template #footer>
      <Button
        label="Cancel"
        text
        severity="secondary"
        @click="isEditDialog = false"
      />
      <Button
        label="Save"
        outlined
        severity="success"
        @click="saveEditedVolume"
      />
    </template>
  </Dialog>
</template>