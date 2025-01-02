<script setup>
import { ref } from 'vue'

const deliveries = ref([
  { 
    code: 'DEL001', 
    clientCode: 'CLIENT1', 
    status: 'PENDING',
    createdAt: '2024-03-15',
    volumes: 3
  },
  { 
    code: 'DEL002', 
    clientCode: 'CLIENT2', 
    status: 'IN_TRANSIT',
    createdAt: '2024-03-14',
    volumes: 1
  },
  { 
    code: 'DEL003', 
    clientCode: 'CLIENT3', 
    status: 'DELIVERED',
    createdAt: '2024-03-13',
    volumes: 2
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
        >
          <template #header>
            <div class="flex justify-between">
              <span class="p-input-icon-left ">
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
          <Column field="volumes" header="Volumes" sortable />
          <Column field="status" header="Status" sortable>
            <template #body="{ data }">
              <Tag
                :value="data.status"
                :severity="statusSeverity[data.status]"
              />
            </template>
          </Column>
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
                  icon="pi pi-pencil"
                  rounded
                  text
                  severity="success"
                  @click="navigateTo(`/deliveries/${slotProps.data.code}/edit`)"
                />
                <Button
                  icon="pi pi-trash"
                  rounded
                  text
                  severity="danger"
                />
              </div>
            </template>
          </Column>
        </DataTable>
      </template>
    </Card>
  </div>
</template>