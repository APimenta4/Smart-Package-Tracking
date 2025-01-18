<template>
  <div class="flex flex-col min-h-screen">
    <AppHeader />
    <Toast position="bottom-right"/>
    <NuxtLayout class="flex-grow">
      <NuxtPage />
    </NuxtLayout>
  </div>
</template>