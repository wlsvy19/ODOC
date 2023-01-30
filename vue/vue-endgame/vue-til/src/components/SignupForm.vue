<template>
  <div class="contents">
    <div class="form-wrapper form-wrapper-sm">
      <!-- 이벤트 v:on -> @ , prevent: 새로고침 막음-->
      <form @submit.prevent="submitForm" class="form">
        <div>
          <label for="username">id: </label>
          <input id="username" type="text" v-model="username" />
        </div>
        <div>
          <label for="password">pw: </label>
          <input id="password" type="text" v-model="password" />
        </div>
        <div>
          <label for="nickname">nickname: </label>
          <input id="nickname" type="text" v-model="nickname" />
        </div>
        <!-- 버튼 클릭시 form태그 에서 명시한 메소드 실행-->
        <button
          v-bind:disabled="!isUsernameValid || !password || !nickname"
          type="submit"
        >
          회원 가입
        </button>
      </form>
      <p class="log">{{ logMessage }}</p>
    </div>
  </div>
</template>

<script>
import { registerUser } from '@/api/index';
import { validateEmail } from '@/utils/validation';
export default {
  computed: {
    isUsernameValid() {
      return validateEmail(this.username);
    },
  },
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
      try {
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
      } catch (error) {
        console.log(error.response.data);
        this.logMessage = `회원가입 중 에러가 발생했습니다!!`;
      } finally {
        // 회원가입 후 폼 초기화
        this.initForm();
      }
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
