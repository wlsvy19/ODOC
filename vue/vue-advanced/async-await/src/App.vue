<!-- eslint-disable vue/require-v-for-key -->
<template>
  <div>
    <button @click="loginUser2">Login</button>
    <h1>List</h1>
    <ul>
      <li v-for="item in items">{{ item }}</li>
    </ul>
  </div>
</template>

<script>
import axios from "axios";

import { handleException } from "./utils/handleException.js";

export default {
  data() {
    return {
      items: [],
    };
  },
  methods: {
    // 비동기처리-네트워크탭 가서 느린3g로 테스트
    // 데이터가 나오지 않았는데 다음로직<로그인되었습니다> 출력
    // Promise 사용한 비동기 처리 문법 -> then과 catch 문법 복잡함\
    // Promise의 then~catch는 네트워크or비동기 처리 예외만 처리
    loginUser1() {
      axios
        .get("https://jsonplaceholder.typicode.com/users/1")
        .then((response) => {
          if (response.data.id === 1) {
            console.log("<로그인 되었습니다1.>");
            axios
              .get("https://jsonplaceholder.typicode.com/todos")
              .then((response) => {
                console.log("<로그인 되었습니다2.>");
                this.items = response.data;
              });
          }
        })
        .catch((error) => console.timeLog(error));
    },

    // async await 문법 적용 -> 변수 사용하는거 처럼 직관적 코드로 변환
    // try~catch는 자바스크립트, 네트워크, 비동기 등등 모든 예외처리 가능
    async loginUser2() {
      try {
        var response = await axios.get(
          "https://jsonplaceholder.typicode.com/users/14"
        );
        if (response.data.id === 1) {
          console.log("<로그인 되었습니다1.>");
          var list = await axios.get(
            "https://jsonplaceholder.typicode.com/todos"
          );
          console.log("<로그인 되었습니다2.>");
          this.items = list.data;
        }
        // 예외처리->Promise then().catch()
        // try안의 에러를 catch에서 다 잡음
      } catch (error) {
        //alert("비동기 신 에러!!", error);
        handleException(error);
      }
    },
  },
};
</script>

<style>
</style>
