<script setup>
import { ref, onMounted } from 'vue';
const route = useRoute();

const delivery = ref({
  code: 'DEL001',
  clientCode: 'CLIENT1',
  status: 'IN_TRANSIT',
  createdAt: '2024-03-15',
  volumes: [
    {
      code: 132,
      packageType: 'FRAGILE',
      products: [
        {
          code: 550,
          description: 'Smart TV',
          quantity: 1
        }
      ],
      sensors: [
        {
          code: 23,
          type: 'TEMPERATURE'
        }
      ]
    }
  ]
});

const statusSeverity = {
  PENDING: 'warning',
  IN_TRANSIT: 'info',
  DELIVERED: 'success'
};

onMounted(() => {
  // TODO: Fetch delivery data using route.params.code
  console.log('Delivery code:', route.params.code);
});
</script>

<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow mb-12">
    <Card class="mt-10">
      <template #title>
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-bold">Delivery Details</h2>
          <Button 
            icon="pi pi-arrow-left" 
            label="Back to Deliveries" 
            text 
            @click="navigateTo('/deliveries')"
          />
        </div>
      </template>
      <template #content>
        <!-- Basic Delivery Info -->
        <div class="grid grid-cols-2 gap-4 mb-6">
          <div class="flex flex-col">
            <span class="text-sm font-medium">Delivery Code</span>
            <span class="text-xl">{{ delivery.code }}</span>
          </div>
          <div class="flex flex-col">
            <span class="text-sm font-medium">Client Code</span>
            <span class="text-xl">{{ delivery.clientCode }}</span>
          </div>
          <div class="flex flex-col">
            <span class="text-sm font-medium">Created At</span>
            <span class="text-xl">{{ delivery.createdAt }}</span>
          </div>
          <div class="flex flex-col">
            <span class="text-sm font-medium">Status</span>
            <Tag
              :value="delivery.status"
              :severity="statusSeverity[delivery.status]"
              class="text-lg"
            />
          </div>
        </div>

        <!-- Volumes Section -->
        <div class="mb-4">
          <h3 class="text-xl font-semibold mb-4">Volumes</h3>
          <DataTable :value="delivery.volumes">
            <Column field="code" header="Code" />
            <Column field="packageType" header="Package Type" />
            <Column header="Products">
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
                    icon="pi pi-eye"
                    text
                    rounded
                    severity="info"
                    @click="navigateTo(`/volumes/${data.code}`)"
                    tooltip="View Volume Details"
                  />
                  <Button
                    icon="pi pi-chart-line"
                    text
                    rounded
                    severity="success"
                    @click="navigateTo(`/volumes/${data.code}/readings`)"
                    tooltip="View Readings"
                  />
                </div>
              </template>
            </Column>
          </DataTable>
        </div>
      </template>
    </Card>
  </div>
</template>