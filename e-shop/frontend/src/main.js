import './assets/main.css'

import apiClient from "@/services/api.js"
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { auth } from './stores/auth'


async function bootstrap() {
  const token = localStorage.getItem('user-token')
  if (token) {
    apiClient.defaults.headers.common['Authorization'] = 'Bearer ' + token
  }

  try {
    const res = await apiClient.get('/me', { withCredentials: true })
    auth.user = res.data
  } catch (e) {
    console.log('Failed to load current user', e);
    auth.user = null
  } finally {
    auth.loaded = true
    createApp(App).use(router).mount('#app')
  }
}

await bootstrap()
