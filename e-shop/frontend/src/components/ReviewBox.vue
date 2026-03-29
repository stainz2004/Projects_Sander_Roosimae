<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  rating: Number,
  text: String,
  reviewAvg: Number,
  reviewCount: Number,
  showSummary: { type: Boolean, default: true },
  canReview: { type: Boolean, default: false },
  shopItemId: { type: Number, required: true } // for review popup
})

const formatReviewLabel = (count) => {
  return count === 1 ? "review" : "reviews";
};

const emit = defineEmits(['submit'])

const isEditing = ref(false)
const localRating = ref(props.rating ?? null)
const localText = ref(props.text ?? '')
const showPopup = ref(false)
const reviews = ref([])
const loadingReviews = ref(false)

watch(
    () => [props.rating, props.text],
    () => {
      localRating.value = props.rating ?? null
      localText.value = props.text ?? ''
    }
)

const openReviewsPopup = async () => {
  showPopup.value = true
  loadingReviews.value = true

  try {
    if (props.shopItemId == null) return;
    const res = await fetch(`/api/public/reviews/items/${props.shopItemId}`)
    reviews.value = await res.json()
  } catch (e) {
    console.error('Failed to load reviews', e)
  } finally {
    loadingReviews.value = false
  }
}

const submit = () => {
  emit('submit', {
    rating: localRating.value,
    text: localText.value
  })
  isEditing.value = false
}
</script>

<template>
<div class ="review-box">
  <p
      v-if="showSummary"
      class="review-info clickable"
      @click="openReviewsPopup">
      ⭐ {{ reviewAvg?.toFixed(1) || 0 }}
      ({{ reviewCount || 0 }} {{ formatReviewLabel(reviewCount || 0) }})
    </p>

    <div v-if="!isEditing">
      <p v-if="text" class="review-text">
        {{ text }}
      </p>

      <button
          v-if="canReview"
          class="edit-btn"
          @click="isEditing = true"
      >
        {{ rating ? 'Edit review' : 'Leave review' }}
      </button>
    </div>

    <div v-else class="review-form">
      <div class="star-picker">
        <span
            v-for="n in 5"
            :key="n"
            class="star"
            :class="{ active: n <= localRating }"
            @click="localRating = n"
        >
          ★
        </span>
      </div>

      <textarea
          v-model="localText"
          placeholder="Write your review…"
          rows="3"
      />

      <div class="actions">
        <button class="cancel-btn" @click="isEditing = false">
          Cancel
        </button>
        <button
            class="submit-btn"
            :disabled="!localRating"
            @click="submit"
        >
          Submit
        </button>
      </div>
  </div>
  <div v-if="showPopup" class="modal-overlay" @click.self="showPopup = false">
    <div class="modal">
      <h3>All Reviews</h3>

      <p v-if="loadingReviews">Loading…</p>

      <div v-else-if="!reviews.length">
        No reviews yet.
      </div>

      <div v-else class="reviews-list">
        <div v-for="r in reviews" :key="r.id" class="review-item">
          <div class="stars">
            ⭐ {{ r.rating }}
          </div>
          <p>{{ r.comment }}</p>
        </div>
      </div>

      <button class="close-btn" @click="showPopup = false">
        Close
      </button>
    </div>
  </div>
</div>
</template>

<style scoped>

.clickable {
  cursor: pointer;
}

.clickable:hover {
  text-decoration: underline;
}

.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background: white;
  border-radius: 10px;
  width: 400px;
  max-height: 70vh;
  padding: 16px;
  overflow-y: auto;
}

.reviews-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.review-item {
  border-bottom: 1px solid #e5e7eb;
  padding-bottom: 8px;
}

.close-btn {
  margin-top: 12px;
  background: #006994;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 6px;
  cursor: pointer;
}

.review-info {
  font-size: 13px;
  color: #374151;
  margin-bottom: 6px;
}

.review-text {
  font-size: 14px;
  color: #111827;
  margin-bottom: 8px;
}

.star-picker {
  display: flex;
  gap: 6px;
  font-size: 22px;
  margin-bottom: 8px;
}

.star {
  cursor: pointer;
  color: #d1d5db;
  transition: color 0.2s, transform 0.1s;
}

.star:hover {
  transform: scale(1.1);
  color: #facc15;
}

.star.active {
  color: #facc15;
}

textarea {
  width: 100%;
  resize: vertical;
  padding: 8px;
  border-radius: 6px;
  border: 1px solid #d1d5db;
  font-family: inherit;
  font-size: 14px;
}

textarea:focus {
  outline: none;
  border-color: #006994;
  box-shadow: 0 0 0 1px #00699433;
}

.actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 10px;
}

.edit-btn {
  background: none;
  border: none;
  color: #006994;
  cursor: pointer;
  font-size: 13px;
  padding: 0;
}

.edit-btn:hover {
  text-decoration: underline;
}

.cancel-btn {
  background: none;
  border: 1px solid #9ca3af;
  color: #374151;
  padding: 6px 12px;
  border-radius: 6px;
  cursor: pointer;
}

.submit-btn {
  background: #006994;
  color: white;
  border: none;
  padding: 6px 14px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
}

.submit-btn:disabled {
  background: #9ca3af;
  cursor: not-allowed;
}
</style>
