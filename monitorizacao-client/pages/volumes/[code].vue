<script setup>
import { ref, onMounted } from 'vue';

const route = useRoute();
const volume = ref(null);

// Mock volume data - replace with API call
const mockVolume = {
  codigo: 42,
  codigoEncomenda: 2,
  estado: 'ENTREGUE',
  dataEnvio: '2024-12-28',
  dataEntrega: '2024-01-08',
  tipoEmbalagem: 'FRAGIL',
  produtos: [
    {
      codigo: 1256,
      descricao: 'LG Smart TV LED UHD 4K',
      categoria: 'ELETRODOMESTICOS',
      unidades: 5
    }
  ],
  sensores: [
    {
      codigo: 345,
      tipo: 'ACELERACAO'
    }
  ]
};

const statusSeverity = {
  PENDENTE: 'warning',
  EM_TRANSITO: 'info',
  ENTREGUE: 'success'
};

onMounted(async () => {
  // TODO: Replace with API call using route.params.code
  volume.value = mockVolume;
});
</script>

<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow mb-12">
    <Card class="mt-10">
      <template #title>
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-bold">Volume Details</h2>
          <Button 
            icon="pi pi-arrow-left" 
            label="Back to Volumes" 
            text 
            @click="navigateTo('/volumes')"
          />
        </div>
      </template>
      <template #content>
        <div v-if="volume" class="flex flex-col gap-6">
          <!-- Basic Volume Info -->
          <div class="grid grid-cols-2 gap-6">
            <div class="flex flex-col gap-2">
              <div>
                <span class="text-sm font-medium">Volume Code</span>
                <div class="text-xl">{{ volume.codigo }}</div>
              </div>
              <div>
                <span class="text-sm font-medium">Delivery Code</span>
                <div class="text-xl">
                  <Button
                    :label="volume.codigoEncomenda.toString()"
                    link
                    @click="navigateTo(`/deliveries/${volume.codigoEncomenda}`)"
                  />
                </div>
              </div>
              <div>
                <span class="text-sm font-medium">Package Type</span>
                <div class="text-xl">{{ volume.tipoEmbalagem }}</div>
              </div>
            </div>

            <div class="flex flex-col gap-2">
              <div>
                <span class="text-sm font-medium">Status</span>
                <div>
                  <Tag
                    :value="volume.estado"
                    :severity="statusSeverity[volume.estado]"
                    class="text-lg"
                  />
                </div>
              </div>
              <div>
                <span class="text-sm font-medium">Sent Date</span>
                <div class="text-xl">{{ volume.dataEnvio }}</div>
              </div>
              <div>
                <span class="text-sm font-medium">Delivery Date</span>
                <div class="text-xl">{{ volume.dataEntrega }}</div>
              </div>
            </div>
          </div>

          <!-- Products Section -->
          <div>
            <h3 class="text-xl font-semibold mb-4">Products</h3>
            <DataTable :value="volume.produtos">
              <Column field="codigo" header="Code" />
              <Column field="descricao" header="Description" />
              <Column field="categoria" header="Category" />
              <Column field="unidades" header="Quantity" />
            </DataTable>
          </div>

          <!-- Sensors Section -->
          <div>
            <h3 class="text-xl font-semibold mb-4">Sensors</h3>
            <DataTable :value="volume.sensores">
              <Column field="codigo" header="Code" />
              <Column field="tipo" header="Type" />
              <Column header="Actions">
                <template #body="{ data }">
                  <div class="flex gap-2">
                    <Button
                      icon="pi pi-chart-line"
                      text
                      rounded
                      severity="info"
                      @click="navigateTo(`/readings?sensor=${data.codigo}`)"
                      tooltip="View Readings"
                    />
                  </div>
                </template>
              </Column>
            </DataTable>
          </div>
        </div>
      </template>
    </Card>
  </div>
</template>