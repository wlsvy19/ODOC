import Vue from 'vue'
import VueRouter from 'vue-router'
import NewsView from '../views/NewsView.vue'
import AskView from '../views/AskView.vue'
import JobsView from '../views/JobsView.vue'
import UserView from '../views/UserView.vue'
import ItemView from '../views/ItemView.vue'
//import createListView from '../views/CreateListView'
import bus from "../utils/bus.js";
import { store } from '../store/index.js'

Vue.use(VueRouter)
export const router = new VueRouter({
  // URL에 #(해쉬)값 제거
  mode: 'history',
  routes: [ //URL 라우팅 정보
    {
      path: '/',
      redirect: '/news' // 홈페이지 접속하자마자 news페이지 이동
    },
    {
      //path: url 주소
      path: '/news',
      // name: 분기처리에 사용
      name: 'news',
      // component: url주소로 갔을 때 표시 될 컴포넌트
      component: NewsView,
      //component: createListView('NewsView',) // 하이오더 컴포넌트 -> CreateListView.vue

      // 특정 URL로 접근할 때 인증 용도
      beforeEnter: (to, from, next) => {
        bus.$emit("start:spinner");

        // 1. 데이터 호출 시점-> actions 호출
        store.dispatch("FETCH_LIST", to.name)  // 어디로 갈거야? = to 에 저장되있음
          .then(() => {
            // bus.$emit("end:spinner");
            next();
          })
          .catch((error) => {
            console.log(error);
          });
      }
    },
    {
      path: '/ask',
      name: 'ask',
      component: AskView,
      //component: createListView('AskView',) // 하이오더 컴포넌트 -> CreateListView.vue

      beforeEnter: (to, from, next) => {
        bus.$emit("start:spinner");
        store.dispatch("FETCH_LIST", to.name)
          .then(() => {
            // bus.$emit("end:spinner");
            next();
          })
          .catch((error) => {
            console.log(error)
          })
      }
    },
    {
      path: '/jobs',
      name: 'jobs',
      component: JobsView,
      //component: createListView('JobsView',) // 하이오더 컴포넌트 -> CreateListView.vue

      beforeEnter: (to, from, next) => {
        bus.$emit("start:spinner");
        store.dispatch("FETCH_LIST", to.name)
          .then(() => {
            // bus.$emit("end:spinner");
            next();
          })
          .catch((error) => {
            console.log(error)
          })
      }
    },
    {
      path: '/user/:id', // route의 params를 받음
      component: UserView
    },
    {
      path: '/item/:id',
      component: ItemView
    }

  ]
})