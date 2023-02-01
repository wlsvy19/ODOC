<template>
  <header>
    <div>
      <router-link to="/" class="logo">
        TIL
        <span v-if="isUserLogin">by {{ $store.state.username }}</span>
      </router-link>
    </div>
    <div class="navigations">
      <!-- ! 로그인 분기 처리 - 보여지는 화면 다름 -->
      <!--  $store.getters.isLogin로 접근할 수 있지만, 템플릿에는 최대한 간결하게 표현->computed 사용-->
      <!-- 1 분기 - 로그인o -->
      <template v-if="isUserLogin">
        <a href="javascript:;" @click="logoutUser" class="logout-button">
          로그아웃
        </a>
      </template>
      <!-- 2 분기 - 로그인x-->
      <template v-else>
        <router-link to="/login">로그인</router-link>
        <router-link to="/signup">회원가입</router-link>
      </template>
    </div>
  </header>
</template>

<script>
// 상대 경로
// import Demo1 from '../../demo/basic/Demo.vue';

// 절대 경로 -> jsconfig.json 에서 설정
// import Demo2 from '@/demo/basic/Demo.vue';

import bus from '@/utils/bus.js';

export default {
  // computed 속성: props, data, store 등의 데이터 변화에 따라 값을 자동으로 계산 할 때 사용하는 연산식
  computed: {
    isUserLogin() {
      return this.$store.getters.isLogin;
    },
  },
  methods: {
    logoutUser() {
      this.$store.commit('clearUsername');
      bus.$emit('show:toast', '로그아웃 했습니다.');
      this.$router.push('/login');
    },
  },
};
</script>

<style scoped>
.username {
  color: white;
}
header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #927dfc;
  z-index: 2;
  box-shadow: 0px 3px 10px rgba(0, 0, 0, 0.05);
}
a {
  color: #dedede;
  font-size: 18px;
}
a.logo {
  font-size: 30px;
  font-weight: 900;
  color: white;
}
.logo > span {
  font-size: 14px;
  font-weight: normal;
}
.navigations a {
  margin-left: 10px;
}
.fixed {
  position: fixed;
  top: 0;
  width: 100%;
}
a.router-link-exact-active {
  color: white;
  font-weight: bold;
}
</style>
