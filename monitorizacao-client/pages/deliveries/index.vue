<script setup>
import { ref } from 'vue'

const config = useRuntimeConfig();
const api = config.public.API_URL;

const deliveries = ref([])

const filters = ref({
  global: { value: null, matchMode: 'contains' }
})

async function fetchDeliveries() {
  try {
    const response = await fetch(`${api}/orders`)
    const data = await response.json()
    deliveries.value = data   
  } catch (error) {
    console.error("Failed to fetch deliveries:", error);
  }
}

onMounted(() => {
  fetchDeliveries()
});
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
            <Column field="volumes.length" header="Volumes" sortable>
            <template #body="slotProps">
                {{ slotProps.data.volumes.length }} {{ slotProps.data.volumes.length === 1 ? 'Volume' : 'Volumes' }}
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
                  icon="pi pi-box"
                  rounded
                  text
                  severity="success"
                  @click="navigateTo(`/deliveries/${slotProps.data.code}/volumes`)"
                />
                <Button
                  icon="pi pi-chart-line"
                  rounded
                  text
                  severity="success"
                  @click="navigateTo(`/deliveries/${slotProps.data.code}/readings`)"
                />
              </div>
            </template>
          </Column>        
        </DataTable>
      </template>
    </Card>
  </div>
</template>