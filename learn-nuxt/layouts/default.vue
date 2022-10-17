<template>
  <div>
    <header>
      <header class="title">
        <nuxt-link class="logo" to="/">Nuxt Shopping</nuxt-link>
      </header>
      <!-- <router-link></router-link> -->
      <!-- 아래와 동일 -->
      <!-- <nuxt-link to="/">홈페이지</nuxt-link> -->
      <!-- <nuxt-link to="/main">메인 페이지</nuxt-link> -->
      <!-- <nuxt-link to="/product">상품 페이지</nuxt-link> -->
    </header>
    <nuxt />
    <!-- <router-view/> -->
  </div>
</template>

<style scoped>
/* 헤더 관련 스타일 */
header {
  height: 60px;
  display: flex;
  align-items: center;
  padding: 0 0.5rem;
}
.logo {
  font-weight: 900;
}
.logo:visited {
  color: inherit;
}
</style>
