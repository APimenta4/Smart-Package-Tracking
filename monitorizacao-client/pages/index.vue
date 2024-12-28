<script setup>
import { ref } from "vue";

const features = [
  {
    description: "Simulate a Sensor",
    icon: "sensors",
  },
  {
    description: "Simulate a Delivery",
    icon: "local_shipping",
  },
  {
    description: "Simulate Logistics",
    icon: "inventory",
  },
  {
    description: "Simulate ...",
    icon: "question_mark",
  },
];

const responsiveOptions = ref([
  {
    breakpoint: "1400px",
    numVisible: 2,
    numScroll: 1,
  },
  {
    breakpoint: "1199px",
    numVisible: 3,
    numScroll: 1,
  },
  {
    breakpoint: "767px",
    numVisible: 2,
    numScroll: 1,
  },
  {
    breakpoint: "575px",
    numVisible: 1,
    numScroll: 1,
  },
]);

const hoveredItem = ref(null);
</script>

<template>
  <div class="w-10/12 mx-auto flex flex-col flex-grow justify-center mb-12">
    <Carousel
      :value="features"
      :numVisible="3"
      :numScroll="1"
      :responsiveOptions="responsiveOptions"
      circular
      :autoplayInterval="7000" 
    >
      <template #item="slotProps">
        <div
          class="border border-surface-200 dark:border-surface-700 rounded m-2.5 p-12 flex flex-col items-center group transition-transform duration-300 transform hover:scale-105"
          :class="{'opacity-60': hoveredItem !== null && hoveredItem.description !== slotProps.data.description}"
          @mouseover="hoveredItem = slotProps.data"
          @mouseleave="hoveredItem = null"
        >
          <div class="mb-4 font-medium text-center text-xl">{{ slotProps.data.description }}</div>
          <div class="flex-grow flex items-center justify-center">
            <span class="material-icons-outlined !text-5xl transition-all duration-300 group-hover:text-orange-500 group-hover:scale-110">
              {{ slotProps.data.icon }}
            </span>
          </div>
        </div>
      </template>
    </Carousel>
  </div>
</template>