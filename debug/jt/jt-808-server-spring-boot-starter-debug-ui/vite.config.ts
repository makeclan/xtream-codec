import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import ElementPlus from 'unplugin-element-plus/vite';
import UnoCSS from 'unocss/vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue(), ElementPlus({}), UnoCSS()],
  server: {
    proxy: {
      "/dashboard-api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        rewrite: (path) =>
            path.replace(/^\/dashboard-api/, "/dashboard-api/v1"),
      },
    },
  }
})
