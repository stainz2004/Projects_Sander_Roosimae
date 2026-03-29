<script setup>
import apiClient from "@/services/api.js";
import { auth } from '@/stores/auth';
import { reactive } from 'vue';
import { useRouter } from 'vue-router';


const router = useRouter()

const customer = reactive({
  username: '',
  password: ''
})

const login = async () => {
  try {
    const response = await apiClient.post('/public/login', customer)

    const token = response.data?.token
    if (token) {
      localStorage.setItem('user-token', token)
      apiClient.defaults.headers.common['Authorization'] = 'Bearer ' + token
    } else {
      console.warn('Login response did not include a token:', response.data)
    }
    try {
      const meRes = await apiClient.get('me')
      console.log("me response:", meRes.data);
      auth.user = meRes.data
    } catch (error) {
      console.warn('Failed to load current user', error);
      auth.user = null
    } finally {
      auth.loaded = true
    }

    alert('Logged in')
    await router.push('/')
  } catch (err) {
    auth.loaded = true
    auth.user = null

    const msg = err.response?.data?.message;
    alert(msg);
  }
}

</script>

<template>
<div class="login">
  <h2>Login</h2>
  <form @submit.prevent="login" class="form">
    <input v-model="customer.username" type="text" placeholder="username" required>
    <input v-model="customer.password" type="text" placeholder="password" required>
    <button type="submit">Login</button>
  </form>
  <div>
    <p>Dont have an account?</p>
    <p>Sign in here <router-link to="/signin">Create account</router-link></p>
  </div>
</div>

</template>
<style scoped>

.login {
  padding-top: 30px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.form {
  display: flex;
  flex-direction: column;
  width: 300px;
  gap: 10px;
}

</style>