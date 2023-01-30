<template>
  <div class="app">
    <!-- 
      <header>
      <router-link to="/login">로그인</router-link> |
      <router-link to="/signup">회원가입</router-link>
    </header> 
  -->

    <AppHeader></AppHeader>
    <div class="app-contents">
      <RouterView></RouterView>
    </div>

    <!--routes/index.js 에서 정의한 path에서 보여지는 실질적인 컴포넌트-->
  </div>
</template>

<script>
import AppHeader from '@/components/common/AppHeader.vue';

export default {
  components: {
    AppHeader,
  },
};
</script>

<style>
@import './css/reset.css';
@import './css/common.css';
</style>
