<template>
  <div class="loading-container">
    <v-progress-circular indeterminate color="#0086E5" size="64" class="ma-2" />
    <div class="loading-text">
      <div class="color-changing-text" v-html="computedMessage"></div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  message: {
    type: String,
    required: false,
    default: '데이터 조회 중입니다.<br />잠시만 기다려 주세요.',
  },
});

const computedMessage = computed(() => {
  if (props.message === '') {
    return '데이터 조회 중입니다.<br />잠시만 기다려 주세요.';
  } else {
    return props.message;
  }
});
</script>

<style scoped>
.loading-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  align-items: center;
}

.color-changing-text {
  font-size: 0.9rem;
  font-weight: bold;
  animation: colorChange 1.8s ease-in-out infinite;
}

@keyframes colorChange {
  0% {
    color: white;
  }
  50% {
    color: red;
  }
  100% {
    color: white;
  }
}
</style>
