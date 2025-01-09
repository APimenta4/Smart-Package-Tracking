<script setup>
import { ref } from 'vue'

const deliveries = ref([
  { 
    code: 'DEL001', 
    clientCode: 'CLIENT1', 
    createdAt: '2024-03-15',
    volumes: [
      {
        code: 'VOL001',
        status: 'PENDING',
        packageType: 'FRAGILE',
        sentDate: null,
        deliveryDate: null
      },
      {
        code: 'VOL002',
        status: 'IN_TRANSIT',
        packageType: 'REGULAR',
        sentDate: '2024-03-16',
        deliveryDate: null
      }
    ]
  },
  { 
    code: 'DEL002', 
    clientCode: 'CLIENT2', 
    createdAt: '2024-03-14',
    volumes: [
      {
        code: 'VOL003',
        status: 'DELIVERED',
        packageType: 'FRAGILE',
        sentDate: '2024-03-14',
        deliveryDate: '2024-03-15'
      }
    ]
  }
])

const filters = ref({
  global: { value: null, matchMode: 'contains' }
})

const statusSeverity = {
  PENDING: 'warning',
  IN_TRANSIT: 'info',
  DELIVERED: 'success'
}

const expandedRows = ref([])
</script>

<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow mb-12">
    <Card class="mt-10">
      <template #title>
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-bold">All Deliveries</h2>
        </div>
      </template>
      <template #content>
        <DataTable
          :value="deliveries"
          paginator
          :rows="10"
          :filters="filters"
          filterDisplay="menu"
          :rowsPerPageOptions="[5, 10, 20]"
          tableStyle="min-width: 50rem"
          :expandedRows="expandedRows"
          @update:expandedRows="expandedRows = $event"
          v-model:expandedRows="expandedRows"
        >
          <template #header>
            <div class="flex justify-between">
              <span class="p-input-icon-left">
                <i class="pi pi-search mr-3" />
                <InputText 
                  v-model="filters['global'].value" 
                  placeholder="Search deliveries..." 
                />
              </span>
            </div>
          </template>

          <Column field="code" header="Delivery Code" sortable />
          <Column field="clientCode" header="Client" sortable />
          <Column field="createdAt" header="Created" sortable />
          <Column field="volumes.length" header="Volumes" sortable />
          <Column header="Actions" :exportable="false">
            <template #body="slotProps">
              <div class="flex gap-2">
                <Button
                  icon="pi pi-eye"
                  rounded
                  text
                  severity="info"
                  @click="navigateTo(`/deliveries/${slotProps.data.code}`)"
                />
                <Button
                  icon="pi pi-box"
                  rounded
                  text
                  severity="success"
                  @click="navigateTo('/', )"
                />
              </div>
            </template>
          </Column>        
        </DataTable>
      </template>
    </Card>
  </div>
</template>