<template>
  <form @submit.prevent="submitForm">
    <div>
      <label for="username">ID: </label>
      <input id="username" type="text" v-model="username" />
    </div>
    <div>
      <label for="password">PW: </label>
      <input id="password" type="text" v-model="password" />
    </div>
    <div>
      <label for="nickname">Nickname: </label>
      <input id="nickname" type="text" v-model="nickname" />
    </div>
    <button type="submit">회원 가입</button>
    <p>{{ logMessage }}</p>
  </form>
</template>

<script>
import { registerUser } from '@/api/index';
export default {
  data() {
    return {
      // 회원가입 들어갈 내용
      username: '',
      password: '',
      nickname: '',

      // 회원가입 결과 뿌려줄 내용
      logMessage: '',
    };
  },
  methods: {
    async submitForm() {
      const userData = {
        username: this.username,
        password: this.password,
        nickname: this.nickname,
      };

      // axios로 서버에 데이터 요청
      // registerUser(userData).then().catch(); async~await문법 처리
      //const response = await registerUser(userData);
      const { data } = await registerUser(userData);
      console.log('응답결과: ', data.username);

      // ``백틱표기법: 자바스크립트 변수를 문자열 안에서 사용 가능
      this.logMessage = `${data.username}님이 가입되었습니다.`;

      // 회원가입 후 폼 초기화
      this.initForm();
    },
    initForm() {
      this.username = '';
      this.password = '';
      this.nickname = '';
    },
  },
};
</script>

<style></style>
