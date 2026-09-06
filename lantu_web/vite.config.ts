import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    allowedHosts: ["68356lmot458.vicp.fun"],
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      },
      '/avatars': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      },
    },
  },
})
