<template>
  <!-- 이벤트 v:on -> @ , prevent: 새로고침 막음-->
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
    <!-- 버튼 클릭시 form태그 에서 명시한 메소드 실행-->
    <button type="submit">회원 가입</button>
    <p>{{ logMessage }}</p>
  </form>
</template>

<script>
import { registerUser } from '@/api/index';
export default {
  data() {
    return {
      // 회원가입 들어갈 내용 초기화 -> v-model input 값과 연동
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

      // axios로 서버에 데이터 요청 -> api 화해서 사용
      // registerUser(userData).then().catch(); -> async~await문법 사용
      //const response = await registerUser(userData);

      const response = await registerUser(userData);
      // {data} -> response.data 함축 (ES6 Destructuring)
      // const { data } = await registerUser(userData);

      // ``백틱표기법: 자바스크립트 변수를 문자열 안에서 사용 가능
      this.logMessage = `${response.data.username}님이 가입되었습니다.`;

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
