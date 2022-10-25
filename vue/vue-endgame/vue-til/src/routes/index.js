import Vue from 'vue';
import VueRouter from 'vue-router';
// jsconfig.json 에서 별칭 설정
import LoginPage from '@/views/LoginPage';
import SignupPage from '@/views/SignupPage';

// 플러그인 초기화(사용)하기 위한 코드
Vue.use(VueRouter);

// VueRouter 객체를 외부에서 사용하기 위함
export default new VueRouter({
  routes: [
    {
      path: '/login',
      component: LoginPage,
    },
    {
      path: '/signup',
      component: SignupPage,
    },
  ],
});
