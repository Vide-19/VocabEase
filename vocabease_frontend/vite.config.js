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
    // 启用 historyApiFallback 以支持 Vue Router 的 History 模式
    historyApiFallback: true,
    proxy: {
      "/api": {
        target: "http://localhost:9091",
        changeOrigin: true,
      },
      // 针对 /index 路径的特殊处理
      '^/index/(getAllData|getWeekAllData|getWeekContentData)': {
        target: 'http://localhost:9091',
        changeOrigin: true,
      },
      '/settings': {
        target: 'http://localhost:9091',
        changeOrigin: true
      },
      '/category': {
        target: 'http://localhost:9091',
        changeOrigin: true
      },
      '/article': {
        target: 'http://localhost:9091',
        changeOrigin: true
      },
      '/word': {
        target: 'http://localhost:9091',
        changeOrigin: true
      },
      '/question': {
        target: 'http://localhost:9091',
        changeOrigin: true
      },
      '/share': {
        target: 'http://localhost:9091',
        changeOrigin: true
      },
      '/appCarousel': {
        target: 'http://localhost:9091',
        changeOrigin: true
      },
      '/file': {
        target: 'http://localhost:9091',
        changeOrigin: true
      },
      '/word2category': {
        target: 'http://localhost:9091',
        changeOrigin: true
      },
      '/article2category': {
        target: 'http://localhost:9091',
        changeOrigin: true
      },
      '/question2category': {
        target: 'http://localhost:9091',
        changeOrigin: true
      },
      '/appFeedback': {
        target: 'http://localhost:9091',
        changeOrigin: true
      },
      '/appAccount': {
        target: 'http://localhost:9091',
        changeOrigin: true
      }
    }
  }
})