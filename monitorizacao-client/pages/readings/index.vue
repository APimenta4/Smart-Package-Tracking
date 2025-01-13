<script setup>
import { ref, onMounted } from 'vue'
import { useRuntimeConfig } from '#imports'

const readings = ref([])
const filters = ref({
  global: { value: null, matchMode: 'contains' }
})

const sensorTypeBadge = {
  LOCATION: 'info',
  TEMPERATURE: 'success', 
  ACCELERATION: 'warning'
}

const config = useRuntimeConfig()
const api = config.public.API_URL

onMounted(async () => {
  try {
    const response = await fetch(`${api}/readings`)
    const data = await response.json()
    readings.value = data.map(reading => ({
      ...reading,
      timestamp: new Date(reading.timestamp).toLocaleString()
    }))
  } catch (error) {
    console.error('Error fetching readings:', error)
  }
})
</script>

<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow mb-12">
    <Card class="mt-10">
      <template #title>
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-bold">All Readings</h2>
        </div>
      </template>
      <template #content>
        <DataTable
          :value="readings"
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
                  placeholder="Search readings..." 
                />
              </span>
            </div>
          </template>

          <Column field="sensorCode" header="Sensor" sortable />
          <Column field="volumeCode" header="Volume" sortable />
          <Column field="type" header="Type" sortable>
            <template #body="{ data }">
              <Tag
                :value="data.type"
                :severity="sensorTypeBadge[data.type]"
              />
            </template>
          </Column>
          <Column field="timestamp" header="Timestamp" sortable />
          <Column header="Value" :exportable="false">
            <template #body="{ data }">
              <div>
                <template v-if="data.type === 'LOCATION'">
                  {{ data.latitude }}, {{ data.longitude }}
                </template>
                <template v-else-if="data.type === 'TEMPERATURE'">
                  {{ data.temperature }}°C
                </template>
                <template v-else-if="data.type === 'ACCELERATION'">
                  {{ data.acceleration }} m/s²
                </template>
              </div>
            </template>
          </Column>
          <Column header="Actions" :exportable="false">
            <template #body="slotProps">
              <div class="flex gap-2">
                <Button
                  icon="pi pi-box"
                  rounded
                  text
                  severity="success" 
                  @click="navigateTo(`/volumes/${slotProps.data.volumeCode}`)"
                  tooltip="View Sensor"
                  tooltipOptions="top"
                />
              </div>
            </template>
          </Column>
        </DataTable>
      </template>
    </Card>
  </div>
</template>