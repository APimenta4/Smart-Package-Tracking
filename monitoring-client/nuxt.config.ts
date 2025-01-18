import Aura from "@primevue/themes/Aura";

// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: "2024-11-01",
  devtools: { enabled: false },
  css: ["~/assets/css/main.css"],
  postcss: {
    plugins: {
      tailwindcss: {},
      autoprefixer: {},
    },
  },
  runtimeConfig: {
    public: {
    API_URL: process.env.API_URL || 'http://localhost:8080/monitoring/api'
    }
  },
  modules: ['@pinia/nuxt',"@primevue/nuxt-module"],
  primevue: {
    options: {
      theme: {
        preset: {
          ...Aura,
          semantic: {
            ...Aura.semantic,
            primary: {
              50: '{amber.50}',
              100: '{amber.100}',
              200: '{amber.200}',
              300: '{amber.300}',
              400: '{amber.400}',
              500: '{amber.500}',
              600: '{amber.600}',
              700: '{amber.700}',
              800: '{amber.800}',
              900: '{amber.900}',
              950: '{amber.950}',
            },
          },
        },
        options: {
          darkModeSelector: ".app-dark",
          cssLayer: {
            name: "primevue",
            order: "tailwind-base, primevue, tailwind-utilities",
          },
        },
      },
    },
  },
  app: {
    head: {
      link: [
        {
          rel: "stylesheet",
          href: "https://fonts.googleapis.com/icon?family=Material+Icons+Outlined",
        },
      ],
    },
  },
  ssr: false,
});