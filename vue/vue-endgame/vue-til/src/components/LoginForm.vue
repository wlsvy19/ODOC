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
    <button type="submit">로그인</button>
    <p>{{ logMessage }}</p>
  </form>
</template>

<script>
import { loginUser } from '@/api/index';
import { validateEmail } from '@/utils/validation';

export default {
  // computed 속성: props, data, store 등의 데이터 변화에 따라 값을 자동으로 계산 할 때 사용하는 연산식
  computed: {
    isUsernameValid() {
      // this.username이 한글자씩 변할 때 마다 validateEmail 호출
      // true false 반환
      return validateEmail(this.username);
    },
  },
  data() {
    return {
      // 로그인 폼에 들어갈 내용
      username: '',
      password: '',

      // 로그인 시 들어가는 로그 메시지
      logMessage: '',
    };
  },
  methods: {
    async submitForm() {
      try {
        // 비즈니스 로직
        const userData = {
          username: this.username,
          password: this.password,
        };
        //const response = await loginUser(userData);
        const { data } = await loginUser(userData);
        // console.log('응답결과: ', data.user.username);
        console.log('토큰: ', data.token);
        this.logMessage = `${data.user.username}님이 로그인 했습니다.`;
      } catch (error) {
        // 에러 핸들링 코드
        // console.log(error.response.data);
        this.logMessage = error.response.data;
      } finally {
        this.initForm();
      }
    }, // end submitForm()
    initForm() {
      this.username = '';
      this.password = '';
    }, // end initForm()
  },
};
</script>

<style></style>
