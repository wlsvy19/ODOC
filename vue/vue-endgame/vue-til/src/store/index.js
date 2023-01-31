import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  // state: 여러 컴포넌트간에 공유되는 데이터
  state: {
    username: '',
    token: '',
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
  },

  // getters: state의 값을 가져옴 -> return 있어야함
  // 인자: (state, )
  getters: {
    isLogin(state) {
      // 빈문자열이 아니면 로그인 상태임
      return state.username !== '';
    },
  },
});
