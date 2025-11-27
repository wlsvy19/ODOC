<template>
  <div class="app-container">
    <AuthLogin v-if="!authStore.isAuthenticated" />
    <AppLayout v-else />
  </div>
</template>

<script setup>
import { onMounted, onBeforeUnmount } from 'vue';

import { useAuthStore } from '@/stores/index';

const authStore = useAuthStore();

onMounted(() => {
  authStore.checkLoginStatus();
  if (authStore.isAuthenticated) {
    authStore.setWorkerNo(localStorage.getItem('loginWorkerNo'));
    authStore.setIcCode(localStorage.getItem('loginIcCode'));
    authStore.setLoginTime(localStorage.getItem('loginTime'));
    authStore.setIP(localStorage.getItem('IP'));
  }
  window.addEventListener('beforeunload', handleBeforeUnload);
});

const handleBeforeUnload = (event) => {
  const message = 'default message';
  // event.returnValue = message;
  return message;
};

onBeforeUnmount(() => {
  window.removeEventListener('beforeunload', handleBeforeUnload);
});
</script>

<style scoped>
.app-container {
  height: 100vh;
}
</style>
