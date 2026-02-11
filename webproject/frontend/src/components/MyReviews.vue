<script setup>
import apiClient from '@/services/api'
import { onMounted, ref } from 'vue'

const reviews = ref([])
const loading = ref(false)
const error = ref(null)

const fetchMyReviews = async () => {
  loading.value = true
  error.value = null
  try {
    const res = await apiClient.get('/public/reviews/me', {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('user-token')}`
      }
    })
    reviews.value = res.data
  } catch (err) {
    error.value = 'Failed to load reviews'
    console.error(err)
  } finally {
    loading.value = false
  }
}

onMounted(fetchMyReviews)
</script>

<template>
  <div>
    <h2>My Reviews</h2>

    <p v-if="loading">Loading reviews…</p>
    <p v-else-if="error">{{ error }}</p>
    <p v-else-if="!reviews.length">
      You haven’t written any reviews yet.
    </p>

    <div v-else class="reviews-list">
      <div
          v-for="r in reviews"
          :key="r.id"
          class="review-card"
      >
        <div class="stars">
          ⭐ {{ r.rating }}
        </div>

        <p>{{ r.comment }}</p>

        <small class="date">
          Reviewed on {{ new Date(r.createdAt).toDateString() }}
        </small>
      </div>
    </div>
  </div>
</template>

<style scoped>
.reviews-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.review-card {
  border: 1px solid #ddd;
  padding: 12px;
  border-radius: 6px;
}

</style>
