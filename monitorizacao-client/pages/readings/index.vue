<script setup>
import { ref } from 'vue'

const readings = ref([])
const filters = ref({
  global: { value: null, matchMode: 'contains' }
})

const sensorTypeBadge = {
  GPS: 'info',
  TEMPERATURA: 'success', 
  ACELERACAO: 'warning'
}

const mockReadings = [
  {
    codigoSensor: 3453,
    codigoVolume: 42,
    tipo: 'GPS',
    coordenadas: [-50.61, 165.97],
    timestamp: '2024-12-28 19:30:23'
  },
  {
    codigoSensor: 3454,
    codigoVolume: 42, 
    tipo: 'TEMPERATURA',
    temperatura: 15,
    timestamp: '2024-12-28 19:25:20'
  },
  {
    codigoSensor: 3455,
    codigoVolume: 42,
    tipo: 'ACELERACAO', 
    aceleracao: 6,
    timestamp: '2024-12-27 19:18:41'
  }
]

onMounted(async () => {
  // TODO: replace with api call
  readings.value = mockReadings
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

          <Column field="codigoSensor" header="Sensor" sortable />
          <Column field="codigoVolume" header="Volume" sortable />
          <Column field="tipo" header="Type" sortable>
            <template #body="{ data }">
              <Tag
                :value="data.tipo"
                :severity="sensorTypeBadge[data.tipo]"
              />
            </template>
          </Column>
          <Column field="timestamp" header="Timestamp" sortable />
          <Column header="Value" :exportable="false">
            <template #body="{ data }">
              <div>
                <template v-if="data.tipo === 'GPS'">
                  {{ data.coordenadas.join(', ') }}
                </template>
                <template v-else-if="data.tipo === 'TEMPERATURA'">
                  {{ data.temperatura }}°C
                </template>
                <template v-else-if="data.tipo === 'ACELERACAO'">
                  {{ data.aceleracao }} m/s²
                </template>
              </div>
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
                  @click="navigateTo(`/volumes/${slotProps.data.codigoVolume}`)"
                  tooltip="View Volume"
                  tooltipOptions="top"
                />
                <Button
                  icon="pi pi-box"
                  rounded
                  text
                  severity="success" 
                  @click="navigateTo(`/sensors/${slotProps.data.codigoSensor}`)"
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