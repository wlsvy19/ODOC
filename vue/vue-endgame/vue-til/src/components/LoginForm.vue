<template>
  <div class="contents">
    <div class="form-wrapper form-wrapper-sm">
      <form @submit.prevent="submitForm" class="form">
        <div>
          <label for="username">id:</label>
          <input id="username" type="text" v-model="username" />
          <p class="validation-text">
            <span class="warning" v-if="!isUsernameValid && username">
              Please enter an email address
            </span>
          </p>
        </div>
        <div>
          <label for="password">pw:</label>
          <input id="password" type="text" v-model="password" />
        </div>
        <button
          :disabled="!isUsernameValid || !password"
          type="submit"
          class="btn"
        >
          로그인
        </button>
      </form>
      <p class="log">{{ logMessage }}</p>
    </div>
  </div>
</template>

<script>
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
        // try: 비즈니스 로직
        const userData = {
          username: this.username,
          password: this.password,
        };

        /**
         * ! store의 index.js 에서 actions로 처리
         */
        // dispatch: actions를 호출
        // async 붙여서 비동기 처리로 쿠키 값 저장 한 후 라우터 진입 해야 함
        await this.$store.dispatch('LOGIN', userData);

        //const response = await loginUser(userData);
        // const { data } = await loginUser(userData);
        // console.log('data token: ', data.token);

        // /**
        //  * TODO: 다음 프로젝트 때 스프링에서 JWT 생성 후 여기다가 담아 줘야함
        //  */
        // // 권한을 위해 토큰을 넘김
        // this.$store.commit('setToken', data.token);

        // // commit: Vuex Store의 mutations를 호출 하는 API -> 데이터를 조작하기 위해 호출
        // this.$store.commit('setUsername', data.user.username);

        // // 쿠키에 토큰과 유저id 저장
        // saveAuthToCookie(data.token);
        // saveUserToCookie(data.user.username);

        /**
         * ! 여기까지
         */

        // 로그인 후 메인페이지로 바로 이동
        // <router-link to="main"> 와 동일
        // https://router.vuejs.org/guide/essentials/navigation.html#navigate-to-a-different-location
        // 이동하면서 파라미터, 쿼리 등도 넘길 수 있음
        this.$router.push('/main');
      } catch (error) {
        // catch: 에러 핸들링 코드
        // console.log(error.response.data);
        this.logMessage = error.response.data;
      } finally {
        // finally: 무조건 실행되는 코드
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

<style>
.btn {
  color: white;
}
</style>
