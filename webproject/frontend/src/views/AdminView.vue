<script setup>
import ShopItemCard from "@/components/ShopItemCard.vue";
import ShopItemForm from "@/components/ShopItemForm.vue";
import apiClient from '@/services/api';
import { auth } from '@/stores/auth';
import axios from "axios";
import { computed, onMounted, ref } from 'vue';
const shopItems = ref([])
const loading = ref(false)
const error = ref(null)
const categories = ref([])
const reviewAvg = ref({})
const reviewCount = ref({})
const selectedImage = ref(null)
const editingItemId = ref(null)

const showAdminLink = computed(() => auth.loaded && auth.user && auth.isAdmin())


const editForm = ref({
  name: '',
  description: '',
  price: null,
  categoryId: null
})

const startEdit = (item) => {
  editingItemId.value = item.id
  editForm.value = {
    name: item.name,
    description: item.description,
    price: item.price,
    categoryId: item.categoryId
  }
}

const cancelEdit = () => {
  editingItemId.value = null
}

const saveEdit = async (id) => {
  await apiClient.put(`/admin/items/${id}`, editForm.value)
  editingItemId.value = null
  await fetchShopItems()
}

const fetchShopItems = async () => {
  loading.value = true
  try {
    const response = await apiClient.get('/public/shop')
    shopItems.value = response.data
    await fetchReviewStats(shopItems.value)
  } catch (err) {
    error.value = 'Failed to load Shop Items'
    console.error('Error:', err)
  } finally {
    loading.value = false
  }
}

const fetchReviewStats = async (items) => {
  for (const item of items) {
    try {
      const avgRes = await apiClient.get(`public/reviews/items/${item.id}/Avg`)
      const countRes = await apiClient.get(`public/reviews/items/${item.id}/Amount`)

      reviewAvg.value[item.id] = avgRes.data
      reviewCount.value[item.id] = countRes.data
    } catch {
      reviewAvg.value[item.id] = 0
      reviewCount.value[item.id] = 0
    }
  }
}

onMounted(() => {
  fetchShopItems()
  fetchCategories()

})

const deleteItem = async (id) => {
  if (confirm('Delete this item?')) {
    await apiClient.delete(`/admin/items/${id}`)
    await fetchShopItems() // Reload list
  }
}
const newShopItem = ref({
  name: '',
  description: '',
  price: null,
  categoryId: null,
  productUrl: ''
})

const addShopItem = async () => {
  try {
    const signRes = await apiClient.get('/admin/image/sign')
    const { cloudName, apiKey, timestamp, signature } = signRes.data
    const formData = new FormData()
    formData.append('file', selectedImage.value)
    formData.append('timestamp', timestamp)
    formData.append('api_key', apiKey)
    formData.append('signature', signature)

    const cloudinaryRes = await axios.post(
        `https://api.cloudinary.com/v1_1/${cloudName}/image/upload`,
        formData
    )
      newShopItem.value.productUrl = cloudinaryRes.data.secure_url

    const response = await apiClient.post('/admin/items', newShopItem.value)
    shopItems.value.push(response.data)  // Add to list
    // Reset form
    Object.assign(newShopItem.value, {name: '', description: '', price: null, categoryId: null, productUrl: ''})
    alert('Shop item added successfully!');
  } catch (e) {
    console.error('Failed to add shop item', e);
    alert('Something went very wrong!')
  }
}

function onImageSelected(file) {
  console.log('Parent received file:', file)
  console.log('selectedImage before:', selectedImage)
  selectedImage.value = file
  console.log('selectedImage after:', selectedImage.value)
}

const category = ref({
  name: '',
})

const fetchCategories = async () => {
  try {
    const response = await apiClient.get('/public/categories')
    categories.value = response.data
  } catch (err) {
    console.error('Failed to load categories', err)
  }
}

const addCategory = async () => {
  const response = await apiClient.post('/admin/categories', category.value)
  categories.value.push(response.data)  // Add to list
  // Reset form
  Object.assign(category.value, {name: ''})
  alert('Category added successfully!');
}

const coupon = ref({
  code: '',
  discount: null
})

const addCoupon = async () => {
  try {
    await apiClient.post('/admin/coupons', coupon.value)
    alert('Coupon added successfully!');
    Object.assign(coupon.value, {code: '', discount: null})
  } catch (err) {
    console.error('Failed to add coupon', err)
    alert('Failed to add coupon')
  }
}

</script>
<template>
  
  <div class="page-container" v-if="showAdminLink">
    <div class="page-card">
    <h1>Shop management</h1>
    <p>This is where we'll manage items in our shop!</p>
    <form @submit.prevent="addCategory">
      <input v-model="category.name" placeholder="Name" required>
      <button type="submit">Create new category!</button>
    </form>
      <ShopItemForm
          v-model="newShopItem"
          :categories="categories"
          submit-text="Create Shop Item"
          :show-cancel="false"
          @image-selected="onImageSelected"
          @submit="addShopItem"
      />

      <form @submit.prevent="addCoupon">
      <input type="text" v-model="coupon.code" placeholder="Enter coupon code" class="coupon-input" />
      <input type="number" v-model.number="coupon.discount" step="0.01" min="0" max="1" placeholder="Enter discount" class="discount-input" />
      <button type="submit" class="add-coupon-btn">Add Coupon</button>
    </form>

    <div v-if="shopItems.length > 0" class="product-grid">
      <ShopItemCard
          v-for="item in shopItems"
          :key="item.id"
          :item="item"
          :review-avg="reviewAvg[item.id]"
          :review-count="reviewCount[item.id]"
          :is-admin="true"
          :shop-item-id="item.id"
      >
        <template #actions>

          <!-- NORMAL MODE -->
          <div v-if="editingItemId !== item.id">
            <button @click="startEdit(item)">Edit</button>
            <button @click="deleteItem(item.id)">Delete</button>
          </div>

          <!-- EDIT MODE -->
          <div v-else>
            <ShopItemForm
                v-model="editForm"
                :categories="categories"
                submit-text="Save"
                :showImageInput="false"
                @submit="saveEdit(item.id)"
                @cancel="cancelEdit"
            />
          </div>

        </template>
      </ShopItemCard>

    </div>
    <p v-else>No items found.</p>
    </div>
  </div>
</template>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
}
th, td {
  padding: 8px 12px;
  border: 1px solid #ddd;
  text-align: left;
}
</style>