<script setup>
import apiClient from '@/services/api';
import { auth } from '@/stores/auth';
import { reactive } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter()

const newCustomer = reactive({
  name: '',
  username: '',
  email: '',
  password: ''
})

const addCustomer = async () => {
  try {
    const response = await apiClient.post('/public/signin', newCustomer)
    const token = response.data.token
    localStorage.setItem('user-token', token)
    apiClient.defaults.headers.common['Authorization'] = "Bearer " + token
    
    // Fetch user data to update auth.user
    const userRes = await apiClient.get('/me', { withCredentials: true })
    auth.user = userRes.data
    
    Object.assign(newCustomer, {name: '', username: '', email: '', password: ''})
    alert("Signed in")
    await router.push('/')
  } catch (err) {
    const msg = err.response?.data?.message;
    alert(msg);
  }
}

</script>

<template>
  <div class="sign-in">
      <h2>
        Signin Form
      </h2>

      <form @submit.prevent="addCustomer" class="form">
        <input v-model="newCustomer.name" type="text" placeholder="name" required>
        <input v-model="newCustomer.username" type="text" placeholder="username" required>
        <input v-model="newCustomer.email" type="email" placeholder="email" required>
        <input v-model="newCustomer.password" type="text" placeholder="password" required>
        <button type="submit">Sign-in</button>
      </form>
  </div>
</template>

<style scoped>

.sign-in {
  margin-top: 30px;
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