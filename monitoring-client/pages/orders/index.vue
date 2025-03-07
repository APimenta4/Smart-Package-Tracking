<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '../store/auth-store'
import { useRouter } from 'vue-router'

const config = useRuntimeConfig();
const api = config.public.API_URL;

const auth = useAuthStore(); 
const router = useRouter();
const deliveries = ref([])

const filters = ref(auth.isAuthenticated ? {
  global: { value: null, matchMode: 'contains' }
} : null);

if (!auth.isAuthenticated || (auth.user.role !== "Client" && auth.user.role !== "Manager")) {
  router.push("/");
}

async function fetchDeliveries() {
  try {
    const response = await fetch(`${api}/orders`, {
      headers: {
        'Authorization': `Bearer ${auth.token}` 
      }
    })
    const data = await response.json()
    deliveries.value = data   
  } catch (error) {
    console.error("Failed to fetch orders:", error);
  }
}

onMounted(() => {
  if (auth.isAuthenticated) {
    fetchDeliveries();
  }
});
</script>

<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow mb-12">
    <Card class="mt-10">
      <template #title>
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-bold">
            {{ auth.user.role === "Client" ? "My Orders" : "All Orders" }}
          </h2>
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
                  placeholder="Search orders..." 
                />
              </span>
            </div>
          </template>

          <Column field="code" header="Order Code" sortable />
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
                  @click="navigateTo(`/orders/${slotProps.data.code}`)"
                />
                <Button
                  icon="pi pi-box"
                  rounded
                  text
                  severity="warning"
                  @click="navigateTo(`/orders/${slotProps.data.code}/volumes`)"
                />
                <Button
                  icon="pi pi-chart-line"
                  rounded
                  text
                  severity="success"
                  @click="navigateTo(`/orders/${slotProps.data.code}/readings`)"
                />
              </div>
            </template>
          </Column>        
        </DataTable>
      </template>
    </Card>
  </div>
</template>