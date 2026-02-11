<script setup>
import MyReviews from '@/components/MyReviews.vue';
import ReviewBox from "@/components/ReviewBox.vue";
import apiClient from '@/services/api';
import { onMounted, ref } from 'vue';


const profile = ref(null)
const orders = ref([])
const loading = ref(false)
const error = ref(null)
const isLoggedIn = ref(false)
const expandedOrderId = ref(null)
const reviewAvg = ref({})
const reviewCount = ref({})
const activeTab = ref('orders')


const loadReviewStatsForOrder = async (order) => {
  for (const item of order.items) {
    const shopItemId = item.shopItemId
    if (shopItemId == null) return;

    // Already loaded → skip
    if (reviewAvg.value[shopItemId] !== undefined) continue

    try {
      const [avgRes, countRes] = await Promise.all([
        apiClient.get(`/public/reviews/items/${shopItemId}/Avg`),
        apiClient.get(`/public/reviews/items/${shopItemId}/Amount`)
      ])

      reviewAvg.value[shopItemId] = avgRes.data
      reviewCount.value[shopItemId] = countRes.data
    } catch {
      reviewAvg.value[shopItemId] = 0
      reviewCount.value[shopItemId] = 0
    }
  }
}

const submitReview = async ({ rating, text }, item) => {
  try {

    const token = localStorage.getItem('user-token');
    const tokenData = JSON.parse(atob(token.split('.')[1]));
    const username = tokenData.sub;

    await apiClient.post(
        'public/reviews',
        {
          customerName: username,
          shopItemId: item.shopItemId,
          rating,
          comment: text
        },
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
    );

    alert(`Review successfully added for "${item.name}"!`);
    item.review = {
      rating,
      text
    }

    // refresh global stats for this product
    console.log('shopItemId', item.shopItemId);
    const [avgRes, countRes] = await Promise.all([
      apiClient.get(`/public/reviews/items/${item.shopItemId}/Avg`),
      apiClient.get(`/public/reviews/items/${item.shopItemId}/Amount`)
    ])

    reviewAvg.value[item.shopItemId] = avgRes.data
    reviewCount.value[item.shopItemId] = countRes.data
  } catch (err) {
    console.error('Error submitting review:', err);
    const msg = err.response?.data?.message;
    alert(msg);
  }
};

const fetchProfile = async () => {
  try {
    const token = localStorage.getItem('user-token')
  if (!token) {
    isLoggedIn.value = false
    return
  }
    const response = await apiClient.get('/me',
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
    )
    profile.value = response.data
    await fetchOrders()
  } catch (err) {
    error.value = 'Failed to load profile'
    console.error(err)
  }
}

const fetchOrders = async () => {
  const token = localStorage.getItem('user-token')
  if (!token) {
    isLoggedIn.value = false
    return
  }
  loading.value = true
  try {
    const response = await apiClient.get(`/order/me`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        })
    orders.value = response.data
  } catch (err) {
    error.value = 'Failed to load orders'
    console.error(err)
  } finally {
    loading.value = false
  }
}

const toggleOrder = async (orderId) => {
  if (expandedOrderId.value === orderId) {
    expandedOrderId.value = null
    return
  }

  expandedOrderId.value = orderId

  const order = orders.value.find(o => o.id === orderId)
  if (!order.items) {
    const res = await apiClient.get(`/order/${orderId}`)
    order.items = res.data.items
  }

  await loadReviewStatsForOrder(order)
}

onMounted(() => {
  fetchProfile()
})
</script>

<template>
  <div class="page-container">
    <div class="page-card">
    <h1>My Profile</h1>
      <div class="profile-tabs">
        <button
            :class="{ active: activeTab === 'orders' }"
            @click="activeTab = 'orders'"
        >
          My Orders
        </button>

        <button
            :class="{ active: activeTab === 'reviews' }"
            @click="activeTab = 'reviews'"
        >
          My Reviews
        </button>
      </div>

      <div v-if="profile">
        <div class="profile-info">
          <p><strong>Username:</strong> {{ profile.username }}</p>
        </div>

        <!-- ORDERS TAB -->
        <div v-if="activeTab === 'orders'">
          <h2>Order History</h2>

          <div
              v-for="order in orders"
              :key="order.id"
              class="order_card"
          >
            <div class="order_summary" @click="toggleOrder(order.id)">
              <div>
                <h3>Order #{{ order.id }}</h3>
                <p><strong>Date:</strong> {{ new Date(order.timestamp).toLocaleString() }}</p>
                <p><strong>Total:</strong> {{ order.price }} €</p>
                <p v-if="order.couponId">
                  <strong>Coupon:</strong> {{ order.couponId }}
                </p>
              </div>

              <div class="expand_hint">
                {{ expandedOrderId === order.id ? '▲ Hide items' : '▼ Show items' }}
              </div>
            </div>

            <table v-if="expandedOrderId === order.id">
              <thead>
              <tr>
                <th>Product</th>
                <th>Category</th>
                <th>Unit</th>
                <th>Qty</th>
                <th>Subtotal</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="item in order.items" :key="item.id">
                <td>
                  {{ item.productName }}
                  <ReviewBox
                      v-if="item.shopItemId !== null"
                      :rating="item.review?.rating"
                      :text="item.review?.text"
                      :review-avg="reviewAvg[item.shopItemId]"
                      :review-count="reviewCount[item.shopItemId]"
                      :show-summary="true"
                      :can-review="true"
                      :shop-item-id="item.shopItemId"
                      @submit="payload => submitReview(payload, item)"
                  />

                  <span v-else class="deleted-product-note">
                    Product no longer available
                  </span>
                </td>
                <td>{{ item.productCategory }}</td>
                <td>{{ item.unitPrice }} €</td>
                <td>{{ item.quantity }}</td>
                <td>{{ (item.unitPrice * item.quantity).toFixed(2) }} €</td>
              </tr>

              <tr class="order-total-row">
                <td colspan="4"><strong>Total</strong></td>
                <td><strong>{{ order.price.toFixed(2) }} €</strong></td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div v-else>
          <MyReviews />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

.profile-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 20px;
}

.profile-tabs button {
  padding: 6px 14px;
  border: 1px solid #ccc;
  background: white;
  cursor: pointer;
  border-radius: 6px;
}

.profile-tabs button.active {
  background: #006994;
  color: white;
  border-color: #006994;
}

.order_summary {
  cursor: pointer;
}

.order_card {
  margin-bottom: 30px;
  padding: 16px;
  border: 1px solid #ddd;
  border-radius: 6px;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

th, td {
  padding: 8px 12px;
  border: 1px solid #ddd;
  text-align: left;
  vertical-align: top;
}
</style>
