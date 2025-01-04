<script setup>
import { ref, onMounted } from 'vue';

const route = useRoute();
const reading = ref(null);

// Mock reading data - replace with API call
const mockReading = {
  codigoSensor: 3453,
  codigoVolume: 42,
  tipo: 'GPS',
  coordenadas: [-50.61, 165.97],
  timestamp: '2024-12-28 19:30:23'
};

const sensorTypeBadge = {
  GPS: 'info',
  TEMPERATURA: 'success', 
  ACELERACAO: 'warning'
};

onMounted(async () => {
  // TODO: Replace with API call using route.params.code
  reading.value = mockReading;
});
</script>

<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow mb-12">
    <Card class="mt-10">
      <template #title>
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-bold">Reading Details</h2>
          <Button 
            icon="pi pi-arrow-left" 
            label="Back to Readings" 
            text 
            @click="navigateTo('/readings')"
          />
        </div>
      </template>
      <template #content>
        <div v-if="reading" class="grid grid-cols-2 gap-6">
          <!-- Basic Reading Info -->
          <div class="flex flex-col gap-2">
            <div>
              <span class="text-sm font-medium">Sensor Code</span>
              <div class="text-xl">{{ reading.codigoSensor }}</div>
            </div>
            <div>
              <span class="text-sm font-medium">Volume Code</span>
              <div class="text-xl">{{ reading.codigoVolume }}</div>
            </div>
            <div>
              <span class="text-sm font-medium">Type</span>
              <div>
                <Tag
                  :value="reading.tipo"
                  :severity="sensorTypeBadge[reading.tipo]"
                  class="text-lg"
                />
              </div>
            </div>
          </div>

          <!-- Reading Value and Timestamp -->
          <div class="flex flex-col gap-2">
            <div>
              <span class="text-sm font-medium">Value</span>
              <div class="text-xl">
                <template v-if="reading.tipo === 'GPS'">
                  {{ reading.coordenadas.join(', ') }}
                </template>
                <template v-else-if="reading.tipo === 'TEMPERATURA'">
                  {{ reading.temperatura }}°C
                </template>
                <template v-else-if="reading.tipo === 'ACELERACAO'">
                  {{ reading.aceleracao }} m/s²
                </template>
              </div>
            </div>
            <div>
              <span class="text-sm font-medium">Timestamp</span>
              <div class="text-xl">{{ reading.timestamp }}</div>
            </div>
          </div>
        </div>

        <!-- Quick Actions -->
        <div class="flex gap-4 mt-8">
          <Button 
            icon="pi pi-box" 
            label="View Volume" 
            @click="navigateTo(`/volumes/${reading?.codigoVolume}`)"
          />
          <Button 
            icon="pi pi-rss" 
            label="View Sensor" 
            @click="navigateTo(`/sensors/${reading?.codigoSensor}`)"
          />
        </div>
      </template>
    </Card>
  </div>
</template>