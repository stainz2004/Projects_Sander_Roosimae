import apiClient from '@/services/api';
import { reactive } from "vue";


export const auth = reactive({
  user: null,
  loaded: false,
  isAdmin() {
    return !!(this.user?.roles?.includes("ROLE_ADMIN"))
  },
async logout() {
    delete apiClient.defaults.headers.common['Authorization']
    this.user = null
    this.loaded = true
    localStorage.removeItem('user-token')
    document.cookie = 'user-token=; Max-Age=0; path=/'
}
}
)