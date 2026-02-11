<script setup>
import { auth } from '@/stores/auth';
import { computed } from 'vue';

const showAdminLink = computed(() => auth.loaded && auth.user && auth.isAdmin())


const logout = () => {
  auth.logout()
  alert('Logged out')
}
</script>

<template>
  <nav>
    <div class="nav-inner">
      <router-link to="/" class="logo-link">
        <img class="home-logo" src="../assets/logo.png" alt="Logo">
      </router-link>

      <div class="nav-spacer"></div>

      <div class="auth-links">
        <RouterLink class="auth-link" v-if="showAdminLink" to="/admin">Admin</RouterLink>
        <router-link class="auth-link" to="/shop">Shop</router-link>
        <router-link class="auth-link" to="/cart">Shopping Cart</router-link>
        <router-link class="auth-link" to="/profile">My Profile</router-link>
        <router-link class="auth-link" to="/login" v-if="!auth.user">Login</router-link>
        <router-link class="auth-link" to="/" v-else @click.prevent="logout">Logout</router-link>
      </div>
    </div>
  </nav>
</template>

<style scoped>

nav {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: #006994;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  z-index: 9999;
}

.nav-inner {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
}

.logo-link {
  display: flex;
  align-items: center;
}

.home-logo {
  height: 50px;
  width: auto;
  max-width: 150px;
}

.nav-spacer {
  flex: 1;
}

.auth-links {
  display: flex;
  align-items: center;
  gap: 40px;
}

.auth-link {
  color: #ffffff;
  font-size: 18px;
  font-weight: 600;
  transition: color 0.7s ease;
}

.auth-link:hover {
  color: #25eae4;
}
/* it actually uses it but sonarqube doesnt like it? */
.auth-link.router-link-active {
  color: #25eae4;
  border-bottom: 2px solid #25eae4;
}

</style>