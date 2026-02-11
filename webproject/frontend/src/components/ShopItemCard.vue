<script setup>
import ReviewBox from "@/components/ReviewBox.vue";

const props = defineProps({
  item: Object,
  reviewAvg: Number,
  reviewCount: Number,
  showReviews: { type: Boolean, default: true },
  isLoggedIn: { type: Boolean, default: false },
  isAdmin: { type: Boolean, default: false },
  shopItemId: { type: Number, required: true } // for review popup
})

const emit = defineEmits(['submit-review']);

const submitReview = (payload) => {
  if (!props.item) return;
  emit('submit-review', {
    item: props.item,
    rating: payload.rating,
    text: payload.text
  });
};

</script>

<template>
  <div v-if="item" class="product-card" >
    <div class="item-image">
      <img
          v-if="item.productUrl"
          :src="item.productUrl"
          :alt="item.name || 'Product image'"
          loading="lazy"
          @error="onImageError"
          class="product-image"
      />
      <div v-else class="placeholder-image">📦</div>
    </div>

    <div class="item-details">
      <h3 class="item-name">{{ item.name }}</h3>
      <p class="item-description">{{ item.description }}</p>

      <ReviewBox
          :rating="item.review?.rating"
          :text="item.review?.text"
          :review-avg="reviewAvg"
          :review-count="reviewCount"
          :show-summary="showReviews"
          :can-review="isLoggedIn"
          :shop-item-id="shopItemId"
          @submit="submitReview"
      />

    </div>

    <div class="item-price">
      <span v-if="item.totalPrice != null" class="price">
        €{{ item.totalPrice.toFixed(2) }}
      </span>
      <span v-else class="price">
        €{{ item.price.toFixed(2) }}

      </span>

      <slot name="actions" />
    </div>
  </div>
</template>


<style>
.product-card {
  display: grid;
  grid-template-columns: 120px 1fr auto;
  gap: 20px;
  padding: 20px;
  border: 1px solid #006994;
  border-radius: 8px;
  transition: box-shadow 0.2s;
}

.item-image {
  width: 160px;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  border-radius: 6px;
  background: #fafafa;
}

.product-image {
  width: 90%;
  height: 90%;
  object-fit: cover;
  display: block;
}

.product-card:hover {
  box-shadow: 8px 8px #d1d1d1;
}

.item-image {
  width: 120px;
  height: 120px;
  background: #006994;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.placeholder-image {
  font-size: 48px;
}

.item-details {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.item-price {
  display: flex;
  align-items: end;
  flex-direction: column;
  padding-top: 4px;
}

.price {
  font-size: 18px;
  font-weight: 700;
  color: #111827;
}

</style>
