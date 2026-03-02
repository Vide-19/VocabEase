import {fileURLToPath, URL} from 'node:url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server:{
    hmr:true,
    port: 5173,
    proxy: {
      "/api": {
        target: "http://localhost:9091",
        changeOrigin: true,
      },
      '/index': {
        target: 'http://localhost:9091', // 你的 Spring Boot 地址
        changeOrigin: true
      },
      '/settings': {
        target: 'http://localhost:9091', // 你的 Spring Boot 地址
        changeOrigin: true
      }

    }
  }
})
