<template>
  <div>
    <header>
      <router-link to="/login">로그인</router-link> |
      <router-link to="/signup">회원가입</router-link>
    </header>
    <!--routes/index.js 에서 정의한 path시에 보여지는 컴포넌트-->
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'app',
  components: {},
};
</script>

<style></style>
