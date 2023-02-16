<template>
  <div>
    <header>
      <header class="title">
        <!-- <router-link> 와 동일-->
        <NuxtLink to="/" class="logo">Nuxt</NuxtLink>
      </header>
      <div class="munu">
        <Nuxt-Link to="/board" class="logo">게시판 </Nuxt-Link>
        <NuxtLink to="/register" class="logo"> 회원가입</NuxtLink>
        <nuxtLink to="/login" class="logo"> 로그인</nuxtLink>
      </div>
    </header>
  </div>
</template>

<script>
export default {}
</script>

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
