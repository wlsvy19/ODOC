<template>
  <div>
    <h1>News...</h1>
    <div v-for="user in users">* {{ user.title }}</div>
  </div>
</template>

<script>
import { fetchNewsList } from "../api/index.js";
export default {
  data() {
    return {
      users: [],
    };
  },
  created() { // 컴포넌트 생성되자마자 들어가는 로직
    var vm = this; // vue 컴포넌트를 바라보는 상태가 아니라서 변수 선언으로 바인딩 해줘야 함
    // 프로미스 기반 API제공: new Promise() 객체 반환

    fetchNewsList()
      .then(function (response) {
        // 콜백함수
        console.log(response);
        vm.users = response.data;
      })
      .catch(function (error) {
        console.log(error);
      });
  },
};
</script>

<style>
</style>