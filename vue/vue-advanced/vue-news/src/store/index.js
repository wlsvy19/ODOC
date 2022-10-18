import Vue from 'vue'
import Vuex from 'vuex'
import mutations from './mutations.js'
import actions from './actions.js'

Vue.use(Vuex)

// Vuex: 상태(여러 컴포넌트 끼리 공유되는 데이터 속성)관리 라이브러리,
export const store = new Vuex.Store({
  state: {
    news: [],
    ask_items: [],
    jobs: []
  },

  getters: { //computed와 동일한 속성인데 store에 있는것일뿐
    fetchedAsk(state) {
      return state.ask_items;
    }
  },

   // state에 접근하기 위해 사용->모듈화 화새 파일로 만듬
  mutations: mutations,

  // mutations에 접근하기 위해 사용->모듈화 해서 파일로 만듬
  actions
})