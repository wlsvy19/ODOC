import Vue from 'vue';
import Vuex from 'vuex';
import {
  getAuthFromCookie,
  getUserFromCookie,
  saveAuthToCookie,
  saveUserToCookie,
} from '@/utils/cookies';
import { loginUser } from '@/api/auth';

Vue.use(Vuex);

export default new Vuex.Store({
  // state: 여러 컴포넌트간에 공유되는 데이터
  state: {
    // 스토어가 생성될 때마다 브라우저의 쿠키에 값 있으면 넣고, 없으면 ''로 초기화
    // 새로고침 해도 토큰이 살아 있음
    username: getUserFromCookie() || '',
    token: getAuthFromCookie() || '',
  },

  // mutations: 데이터를 조작-> state를 바꿈
  // 인자: (state, commit시 두번째 인자로 넘긴 값)
  mutations: {
    // 로그인 했을 때 username 세팅
    setUsername(state, username) {
      state.username = username;
    },
    // 로그아웃 했을 때 username 공백 처리
    clearUsername(state) {
      state.username = '';
    },

    // commit을 하면서 여기다가 토큰 넘길거임
    setToken(state, token) {
      state.token = token;
    },

    // JWT 초기화
    clearToken(state) {
      state.token = '';
    },
  },

  // getters: state의 값을 가져옴 -> return 있어야함
  // 인자: (state, )
  getters: {
    isLogin(state) {
      // 빈문자열이 아니면 로그인 상태임
      return state.username !== '';
    },
  },

  // LoginForm.vue 의 코드가 많아져서 actions 사용
  // actions 인자에 context 있고 그안에 commit 있음
  actions: {
    async LOGIN({ commit }, userData) {
      const { data } = await loginUser(userData);
      console.log('data token: ', data.token);

      /**
       * TODO: 다음 프로젝트 때 스프링에서 JWT 생성 후 여기다가 담아 줘야함
       */
      // 권한을 위해 토큰을 넘김
      commit('setToken', data.token);

      // commit: Vuex Store의 mutations를 호출 하는 API -> 데이터를 조작하기 위해 호출
      commit('setUsername', data.user.username);

      // 쿠키에 토큰과 유저id 저장
      saveAuthToCookie(data.token);
      saveUserToCookie(data.user.username);

      return data;
    },
  },
});
