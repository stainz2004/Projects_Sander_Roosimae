<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import apiClient from '@/services/api';
import PriceFilter from '@/components/PriceFilter.vue';
import ShopItemCard from '@/components/ShopItemCard.vue'


// SHOP DATA
const shopItem = ref([]);
const error = ref(null);
const sortOption = ref("price_asc");
const rangeError = ref("");
const categories = ref([]);
const selectedCategories = ref([]);
const reviewAvg = ref({});
const reviewCount = ref({});


const priceRange = ref({ min: 0, max: 500 });

// LOGIN CHECK
const token = localStorage.getItem("user-token");
const isLoggedIn = ref(!!token);


// Load categories
const fetchCategories = async () => {
  try {
    const response = await apiClient.get('/public/categories');
    categories.value = response.data;
  } catch (err) {
    console.error("Failed to load categories", err);
  }
};

// Load shop items
const fetchShopItems = async () => {
  error.value = null;
  try {
    const response = await apiClient.get('/public/shop', {
      params: {
        minPrice: priceRange.value.min,
        maxPrice: priceRange.value.max,
        categories: selectedCategories.value,
        sort: sortOption.value
      }
    });
    shopItem.value = response.data;

    await fetchReviewStats(shopItem.value);
  } catch (err) {
    error.value = 'Failed to load products';
    console.error(err);
  }
};

const fetchReviewStats = async (items) => {
  for (const item of items) {
    try {
      const avgRes = await apiClient.get(`/public/reviews/items/${item.id}/Avg`);
      const countRes = await apiClient.get(`/public/reviews/items/${item.id}/Amount`);

      reviewAvg.value[item.id] = avgRes.data;
      reviewCount.value[item.id] = countRes.data;

    } catch (err) {
      console.error("Failed to load review stats for", item.id, err);
      reviewAvg.value[item.id] = 0;
      reviewCount.value[item.id] = 0;
    }
  }
};

onMounted(() => {
  fetchShopItems();
  fetchCategories();
});

// PRICE RANGE UPDATE
const onRangeUpdate = (range) => {
  if (range.max < range.min) {
    rangeError.value = "Invalid price range: max price cannot be smaller than min price.";
    return;
  }
  rangeError.value = "";
  priceRange.value = range;
  fetchShopItems();
};

// ADD TO CART
const addItemToCart = async (id) => {
  try {
    const token = localStorage.getItem('user-token');
    await apiClient.post(
        `/cart`,
        { shopItemId: id, quantity: 1 },
        { headers: { Authorization: `Bearer ${token}` } }
    );
    alert("Item added to cart!");
  } catch (err) {
    console.error('Error adding to cart:', err);
    alert("Failed to add item to cart");
  }
};


// SUBMIT REVIEW
const submitReview = async ({item, rating, text}) => {
  try {

    const token = localStorage.getItem('user-token');
    const tokenData = JSON.parse(atob(token.split('.')[1]));
    const username = tokenData.sub;

    await apiClient.post(
        '/public/reviews',
        {
          customerName: username,
          shopItemId: item.id,
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
    await fetchShopItems();

  } catch (err) {
    console.error('Error submitting review:', err);
    const msg = err.response?.data?.message;
    alert(msg);
    }
};
// Listeners
// when categories change → refetch
watch(selectedCategories, () => {
  fetchShopItems();
});

// when sorting changes → refetch
watch(sortOption, () => {
  fetchShopItems();
});

// Filter and sort
const filteredAndSortedItems = computed(() => shopItem.value);
</script>

<template>
  <div class="page-container">
    <div class="page-card">

    <h1 class="title">Here's my Shop!</h1>

    <PriceFilter @update-range="onRangeUpdate" />
    <p v-if="rangeError" class="error">{{ rangeError }}</p>

    <!-- CATEGORY FILTER -->
    <div class="category-filter">
      <h3>Categories</h3>
      <div v-for="cat in categories" :key="cat.id" class="cat-option">
        <label>
          <input type="checkbox" :value="cat.id" v-model="selectedCategories">
          {{ cat.name }}
        </label>
      </div>
    </div>

    <!-- SORTING -->
    <div class="sort-bar">
      <label for="sort-select">Sort by:</label>
      <select id="sort-select" v-model="sortOption">
        <option value="price_asc">Price (Low → High)</option>
        <option value="price_desc">Price (High → Low)</option>
        <option value="name_asc">Name (A → Z)</option>
        <option value="name_desc">Name (Z → A)</option>
      </select>
    </div>

    <!-- PRODUCT GRID -->
    <div v-if="filteredAndSortedItems.length > 0" class="product-grid">
      <ShopItemCard
          v-for="item in filteredAndSortedItems"
          :key="item.id"
          :item="item"
          :review-avg="reviewAvg[item.id]"
          :review-count="reviewCount[item.id]"
          :is-logged-in="isLoggedIn"
          :shopItemId="item.id"
          @submit-review="submitReview"
      >
        <template #actions>
          <button @click="addItemToCart(item.id)" class="cart-btn">
            Add to Cart
          </button>
        </template>
      </ShopItemCard>
      </div>
    </div>
  </div>
</template>

<style scoped>

.title {
  font-size: 28px;
  margin-bottom: 20px;
}

.product-grid {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.cart-btn {
  border: none;
  background: #045e07;
  color: white;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  width: 100px;
}

.sort-bar {
  margin-bottom: 10px;
}

.cart-btn:hover {
  background: #45a049;
}

</style>
