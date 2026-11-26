import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        // Mặc định trỏ về backend tự code (8080). Đổi sang 8081 nếu muốn chạy với reference/backend.
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
