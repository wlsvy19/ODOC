<template>
  <v-snackbar v-model="show" :timeout="timeout" vertical>
    {{ message }}
  </v-snackbar>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
  message: {
    type: String,
    required: true,
  },
  timeout: {
    type: Number,
    default: 3000,
  },
});

const show = ref(props.modelValue);

watch(
  () => props.modelValue,
  (newVal) => {
    show.value = newVal;
  },
);

const emit = defineEmits(['update:modelValue']);

watch(show, (newVal) => {
  emit('update:modelValue', newVal);
});
</script>
