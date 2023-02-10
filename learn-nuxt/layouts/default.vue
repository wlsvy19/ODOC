<template>
  <div>
    <header>
      <header class="title">
        <!-- <router-link> 와 동일-->
        <NuxtLink to="/" class="logo">Nuxt</NuxtLink>
      </header>
      <nuxt-link to="main" class="logo">메인</nuxt-link>
      <Nuxt-Link to="/board" class="logo">게시판 </Nuxt-Link>
      <NuxtLink to="/register" class="logo"> 회원가입</NuxtLink>
      <NuxtLink to="/login" class="logo"> 로그인</NuxtLink>
    </header>
    <!-- <Nuxt>: page폴더 안에 루트 / 라우터 -> index.vue -->
    <nuxt />
  </div>
</template>

<style scoped>
/* 헤더 관련 스타일 */
header {
  height: 70px;
  display: flex;
  align-items: center;
  padding: 0 0.5rem;
}
.logo {
  font-weight: 900;
  margin-left: 100px;
}
.logo:visited {
  color: inherit;
}
</style>
