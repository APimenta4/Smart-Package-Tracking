<template>
  <NuxtLoadingIndicator />
  <div class="flex flex-col min-h-screen">
    <AppHeader />
    <NuxtLayout class="flex-grow">
      <NuxtPage />
    </NuxtLayout>
  </div>
</template>