import Vue from 'vue'
import Vuex from 'vuex'
import todoApp from './modules/todoApp'

Vue.use(Vuex)

// store를 다른 파일 사용 가능 -> main.js 에서 사용할래
export const store = new Vuex.Store({
    modules:{
        todoApp
    }
});