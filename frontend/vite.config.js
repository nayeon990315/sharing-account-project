import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: { '^/api': '' },
      },
      // S3 버킷에 대한 프록시 설정
      "/s3-bucket": {
        target: "https://beeroutine.s3.ap-northeast-2.amazonaws.com",
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path.replace(/^\/s3-bucket/, '')
      },
       // 이미지 파일 경로에 대한 프록시 설정
       '/images': {
        target: 'http://localhost:8080',  // 백엔드 서버
        changeOrigin: true,
      },
    },
  }
})
