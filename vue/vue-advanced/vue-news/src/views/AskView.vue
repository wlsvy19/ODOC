<template>
  <div>
    <h1>Ask...</h1>
    <!--  v-for디렉티브는 in 뒤에 오는 ask배열을 반복하여 div태그 돌림, 결과를 in 앞에 있는 변수로 받음 -->
    <div v-for="item in ask">* {{ item.title }}</div>
  </div>
</template>

<script>
import { fetchAskList } from "../api/index.js";
export default {
  data: function () {
    return {
      ask: [],
    };
  },
  created() {
    var vm = this;
    // 속성에 함수 붙으면 : fuction 생략 가능
    fetchAskList()
      .then(function (response) {
        console.log(response.data)
        vm.ask = response.data;
      })
      .catch(function (error) {
        console.log(error);
      });
  },
};
</script>

<style>
</style>