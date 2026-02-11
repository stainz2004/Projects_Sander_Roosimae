<template>
  <div class="filter-box">
    <h3>Filter by Price</h3>

    <p>Price: €{{ localRange.min }} – €{{ localRange.max }}</p>

    <!-- MIN SLIDER -->
    <input
        type="range"
        v-model.number="localRange.min"
        :min="min"
        :max="max"
        @change="emitRange"
    />

    <!-- MAX SLIDER -->
    <input
        type="range"
        v-model.number="localRange.max"
        :min="min"
        :max="max"
        @change="emitRange"
    />
  </div>
</template>

<script setup>
import { ref } from "vue";

const emit = defineEmits(["update-range"]);

const min = 0;
const max = 500;

const localRange = ref({ min: 0, max: 500 });

function emitRange() {
  emit("update-range", { ...localRange.value });
}
</script>

<style scoped>
.filter-box {
  width: 220px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 20px;
}
</style>
