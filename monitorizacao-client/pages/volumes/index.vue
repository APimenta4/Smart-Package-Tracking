<script setup>
import { ref } from 'vue'

const volumes = ref([])
const filters = ref({
  global: { value: null, matchMode: 'contains' }
})

const statusSeverity = {
  PENDING: 'warning',
  IN_TRANSIT: 'info',
  DELIVERED: 'success'
}

const mockVolumes = [
  {
    code: 42,
    deliveryCode: 2,
    status: 'DELIVERED',
    sentDate: '2024-12-28',
    deliveryDate: '2024-01-08',
    packageType: 'FRAGILE',
    products: [
      {
        code: 1256,
        description: 'LG Smart TV LED UHD 4K',
        category: 'ELETRODOMESTICOS',
        quantity: 5
      }
    ],
    sensors: [
      {
        code: 345,
        type: 'ACELERACAO'
      }
    ]
  }
]


onMounted(async () => {
  // TODO: call api
  volumes.value = mockVolumes
})
</script>

<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow mb-12">
    <Card class="mt-10">
      <template #title>
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-bold">All Volumes</h2>
        </div>
      </template>
      <template #content>
        <DataTable
          :value="volumes"
          paginator
          :rows="10"
          :filters="filters"
          filterDisplay="menu"
          :rowsPerPageOptions="[5, 10, 20]"
          tableStyle="min-width: 50rem"
        >
          <template #header>
            <div class="flex justify-between">
              <span class="p-input-icon-left">
                <i class="pi pi-search mr-3" />
                <InputText 
                  v-model="filters['global'].value"
                  placeholder="Search volumes..." 
                />
              </span>
            </div>
          </template>

          <Column field="code" header="Volume Code" sortable />
          <Column field="deliveryCode" header="Delivery" sortable />
          <Column field="packageType" header="Package Type" sortable />
          <Column field="status" header="Status" sortable>
            <template #body="{ data }">
              <Tag
                :value="data.status"
                :severity="statusSeverity[data.status]"
              />
            </template>
          </Column>
          <Column field="sentDate" header="Sent Date" sortable />
          <Column field="deliveryDate" header="Delivery Date" sortable />
          <Column header="Actions" :exportable="false">
            <template #body="slotProps">
              <div class="flex gap-2">
                <Button
                  icon="pi pi-eye"
                  rounded
                  text
                  severity="info"
                  @click="navigateTo(`/volumes/${slotProps.data.code}`)"
                />
                <Button
                  icon="pi pi-chart-line"
                  rounded
                  text
                  severity="success"
                  @click="navigateTo(`/volumes/${slotProps.data.code}/readings`)"
                />
              </div>
            </template>
          </Column>
        </DataTable>
      </template>
    </Card>
  </div>
</template>