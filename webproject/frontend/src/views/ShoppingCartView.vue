<script setup>
import ShopItemCard from "@/components/ShopItemCard.vue";
import apiClient from '@/services/api';
import {onMounted, ref } from 'vue';

const items = ref([])
const loading = ref(false)
const error = ref(null)
const isLoggedIn = ref(true)
const reviewAvg = ref({});
const reviewCount = ref({});
const buying = ref(false)

const couponCode = ref('')
const appliedCoupon = ref(null)

const cartSummary = ref({
  subtotal: 0,
  discount: 0,
  total: 0,
  couponCode: null
})

const fetchItems = async () => {
  const token = localStorage.getItem('user-token')
  if (!token) {
    isLoggedIn.value = false
    return
  }
  loading.value = true
  error.value = null
  try {
    const response = await apiClient.get('cart')
    items.value = response.data
    console.log('Loaded cart items:', items.value)
    await fetchReviewStats(items.value);
    await fetchCartSummary()
  } catch (err) {
    if (err.response?.status === 401) {
      isLoggedIn.value = false
    } else {
      error.value = 'Failed to load cart'
    }
  } finally {
    loading.value = false
  }
}

const fetchCartSummary = async () => {
  try {
    const params = appliedCoupon.value ? { couponCode: appliedCoupon.value } : {}
    const response = await apiClient.get('cart/summary', { params })
    cartSummary.value = response.data
  } catch (err) {
    console.error('Failed to fetch cart summary', err)
  }
}

const clearCart = async () => {
  if (confirm('Are you sure you want to clear your entire cart?')) {
    try {
      await apiClient.delete('cart')
      appliedCoupon.value = null
      await fetchItems()
    } catch (err) {
      console.error('Error clearing cart:', err)
      error.value = 'Failed to clear cart'
    }
  }
}

const removeItem = async (id) => {
  if (confirm('Remove this item from cart?')) {
    try {
      await apiClient.delete(`cart/${id}`)
      await fetchItems()
    } catch (err) {
      console.error('Error removing item:', err)
      error.value = 'Failed to remove item'
    }
  }
}

const increaseQuantity = async (id) => {
  try {
    await apiClient.put(`cart/${id}/increase`)
    await fetchItems()
  } catch (err) {
    console.error('Failed to increase quantity', err)
    alert('Failed to increase quantity')
  }
}

const decreaseQuantity = async (id) => {
  try {
    await apiClient.put(`cart/${id}/decrease`)
    await fetchItems()
  } catch (err) {
    console.error('Failed to decrease quantity', err)
    alert('Failed to decrease quantity')
  }
}

const submitReview = async ({item, rating, text }) => {
  try {
    const token = localStorage.getItem('user-token')
    const tokenData = JSON.parse(atob(token.split('.')[1]))
    const username = tokenData.sub

    await apiClient.post(
        '/reviews',
        {
          customerName: username,
          shopItemId: item.shopItemId,
          rating: rating,
          comment: text
        },
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }

    )

    alert(`Review successfully added for "${item.name}"!`)
    await fetchReviewStats([item]);

  } catch (err) {
    console.error('Error submitting review:', err)
    alert('You have already added a review for this product or failed to submit review. ')
  }
}

const fetchReviewStats = async (items) => {
  for (const item of items) {
    try {
      const shopItemId = item.shopItemId

      const avgRes = await apiClient.get(`/public/reviews/items/${shopItemId}/Avg`)
      const countRes = await apiClient.get(`/public/reviews/items/${shopItemId}/Amount`)

      reviewAvg.value[shopItemId] = avgRes.data
      reviewCount.value[shopItemId] = countRes.data

    } catch (err) {
      console.error("Failed to load review stats for", item.id, err);
      reviewAvg.value[item.id] = 0;
      reviewCount.value[item.id] = 0;
    }
  }
};

const buyNow = async () => {
  if (items.value.length === 0) {
    alert('Your cart is empty')
    return
  }

  if (!confirm('Place order now?')) return

  buying.value = true
  try {
    const response = await apiClient.post('/order', {
      couponCode: appliedCoupon.value
    })

    alert(`Order #${response.data.id} placed successfully!`)

    items.value = []
    couponCode.value = ''
    appliedCoupon.value = null
    cartSummary.value = { subtotal: 0, discount: 0, total: 0, couponCode: null }

  } catch (err) {
    console.error('Order failed', err)
    alert('Failed to place order')
  } finally {
    buying.value = false
  }
}

const applyCoupon = async () => {
  if (!couponCode.value.trim()) {
    alert('Please enter a coupon code')
    return
  }
  try {
    const response = await apiClient.get(`/public/coupons/${couponCode.value}`)

    appliedCoupon.value = response.data.code
    await fetchCartSummary()

    alert(`Coupon applied! ${(response.data.discount * 100).toFixed(0)}% discount`)
    couponCode.value = ''
  } catch (err) {
    alert('Failed to apply coupon')
    console.error("adding coupon failed", err)
    couponCode.value = ''
  }
}

const removeCoupon = async () => {
  appliedCoupon.value = null
  await fetchCartSummary()
}

onMounted(() => {
  fetchItems()
})
</script>

<template>
  <div class="page-container">
    <div class="page-card">

        <h1>Shopping Cart</h1>
        <span
            v-if="isLoggedIn"
            class="item-count"
        >
          {{ items.length }} item{{ items.length !== 1 ? 's' : '' }}
        </span>


      <div v-if="error" class="error-message">
        {{ error }}
      </div>

      <div v-if="!isLoggedIn" class="login-required">
        <h2>Ostukorvi kasutamiseks logige sisse</h2>
        <router-link to="/login" class="login-btn">
          Logi sisse
        </router-link>
      </div>

      <div v-if="isLoggedIn">

        <div v-if="items.length > 0">

        <div class="cart-items">
          <ShopItemCard
              v-for="item in items"
              :key="item.id"
              :item="item"
              :review-avg="reviewAvg[item.shopItemId]"
              :review-count="reviewCount[item.shopItemId]"
              :is-logged-in="false"
              :shop-item-id="item.shopItemId"
              @submit-review="submitReview"
          >
            <template #actions>
              <div class="quantity-controls">
                <button @click="decreaseQuantity(item.id)" class="quantity-btn">−</button>
                <span class="quantity-display">{{ item.quantity }}</span>
                <button @click="increaseQuantity(item.id)" class="quantity-btn">+</button>
              </div>
              <button @click="removeItem(item.id)" class="remove-btn">Remove from cart</button>
            </template>
          </ShopItemCard>
        </div>

        <div class="coupon-price">
          <div v-if="!appliedCoupon">
            <form @submit.prevent="applyCoupon">
              <input
                  type="text"
                  v-model="couponCode"
                  placeholder="Enter coupon code"
                  class="coupon-input"
              />
              <button type="submit" class="apply-coupon-btn">
                Apply Coupon
              </button>
            </form>
          </div>

          <div v-else class="applied-coupon">
            <span>{{ cartSummary.couponCode }} (-{{ ((cartSummary.discount / cartSummary.subtotal) * 100).toFixed(0) }}%)</span>
            <button @click="removeCoupon" class="remove-coupon-btn">Remove</button>
          </div>

          <div class="total-section">
            <div>Subtotal: €{{ cartSummary.subtotal.toFixed(2) }}</div>
            <div v-if="appliedCoupon" class="discount-line">
              Discount: -€{{ cartSummary.discount.toFixed(2) }}
            </div>
            <div class="total-amount">Total: €{{ cartSummary.total.toFixed(2) }}</div>
          </div>
        </div>

        <div class="cart-footer">
          <button @click="clearCart" class="clear-cart-btn">
            Clear Cart
          </button>

          <button
              @click="buyNow"
              :disabled="buying"
              class="buy-btn"
          >
            {{ buying ? 'Placing order...' : 'Order now' }}
          </button>
        </div>
      </div>

      <div v-else class="empty-cart">
        <div class="empty-icon">🎣</div>
        <h2>Your Fiish shopping cart is empty</h2>
        <p>Add items to get started!</p>
      </div>
  </div>
    </div>
  </div>
</template>

<style scoped>

.buy-btn {
  background: #006994;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
}
.coupon-price {
  display:flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: flex-start;
  margin-top: 20px;
  padding: 16px;
  background: #f9fafb;
  border-radius: 8px;
}
.buy-btn:disabled {
  background: #9ca3af;
  cursor: not-allowed;
}

.cart-header h1 {
  font-size: 28px;
  font-weight: 400;
  color: #111827;
}

.item-count {
  color: #2a2a2a;
  font-size: 28px;
}

.cart-items {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.remove-btn {
  border: none;
  background: none;
  color: #0066c0;
  font-size: 13px;
  cursor: pointer;
  padding: 0;
  width: fit-content;
  text-align: left;
}

.remove-btn:hover {
  color: #c45500;
  text-decoration: underline;
}

.quantity-controls {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.quantity-btn {
  width: 32px;
  height: 32px;
  border: 1px solid #d1d5db;
  background: white;
  border-radius: 4px;
  cursor: pointer;
  font-size: 18px;
  font-weight: bold;
}

.quantity-display {
  min-width: 40px;
  text-align: center;
  font-size: 20px;
  font-weight: 600;
  color: #111827;
  padding: 0 8px;
}

.applied-coupon {
  display: flex;
  gap: 8px;
  align-items: center;
  padding: 8px 12px;
  background: #d1fae5;
  border-radius: 4px;
}

.remove-coupon-btn {
  background: none;
  border: none;
  color: #dc2626;
  cursor: pointer;
  font-size: 13px;
  text-decoration: underline;
}

.discount-line {
  color: #10b981;
  font-weight: 600;
}

.total-amount {
  font-size: 18px;
  font-weight: 900;
  color: #b12704;
  padding-top: 8px;
  border-top: 1px solid #d1d5db;
}

.cart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #e5e7eb;
}

.clear-cart-btn {
  background: none;
  border: 1px solid #dc2626;
  color: #dc2626;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

.empty-cart {
  text-align: center;
  padding: 60px 20px;
}

.empty-icon {
  font-size: 80px;
  margin-bottom: 20px;
  opacity: 0.5;
}

.empty-cart h2 {
  font-size: 24px;
  font-weight: 400;
  color: #111827;
  margin: 0 0 8px 0;
}

.empty-cart p {
  color: #6b7280;
  margin: 0;
}

.login-required {
  text-align: center;
  padding: 60px 20px;
}
</style>