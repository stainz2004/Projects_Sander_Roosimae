<script setup>
const props = defineProps({
  modelValue: Object,
  categories: Array,
  submitText: { type: String, default: 'Save' },
  showCancel: { type: Boolean, default: true },
  showImageInput: { type: Boolean, default: true }
})

const emit = defineEmits(['update:modelValue', 'submit', 'cancel', 'image-selected'])

const onFileChange = (e) => {
  const file = e.target.files[0]
  if (file) {
    emit('image-selected', file)
  }
}

const update = (key, value) => {
  emit('update:modelValue', { ...props.modelValue, [key]: value })
}
</script>

<template>
  <div class="shop-item-form">
    <input
        :value="modelValue.name"
        @input="update('name', $event.target.value)"
        placeholder="Name"
        required
    />

    <input
        :value="modelValue.description"
        @input="update('description', $event.target.value)"
        placeholder="Description"
    />

    <input
        type="number"
        step="0.01"
        :value="modelValue.price"
        @input="update('price', Number($event.target.value))"
        placeholder="Price"
        required
    />

    <select
        :value="modelValue.categoryId"
        @change="update('categoryId', Number($event.target.value))"
        required
    >
      <option disabled value="">Select category</option>
      <option v-for="cat in categories" :key="cat.id" :value="cat.id">
        {{ cat.name }}
      </option>
    </select>
    <input
        v-if="showImageInput"
        type="file"
        accept="image/*"
        @change="onFileChange"
    />
    <button @click="$emit('submit')">{{ submitText }}</button>
    <button v-if="showCancel" @click="$emit('cancel')">Cancel</button>
  </div>
</template>
