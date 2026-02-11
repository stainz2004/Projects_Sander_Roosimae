<script setup>
import NavBar from "@/components/NavBar.vue";
</script>

<template>
  <div id="app">
    <NavBar />
    <main class="main-content">
      <RouterView />
    </main>
  </div>
</template>

<style>
* {
  box-sizing: border-box;
}

html, body {
  margin: 0;
  font-family: Arial, sans-serif;
  background-color: #f3ffeb;
}

#app {
  width: 100%;
}

.main-content {
  width: 100%;
}
</style>