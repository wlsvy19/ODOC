import Vue from 'vue';
import VueRouter from 'vue-router';
// jsconfig.json 에서 별칭 설정
// import LoginPage from '@/views/LoginPage';
// import SignupPage from '@/views/SignupPage';

// 플러그인 초기화(사용)하기 위한 코드
Vue.use(VueRouter);

// VueRouter 객체를 외부에서 사용하기 위함
export default new VueRouter({
  // URL # 제거 -> 서버에 fallback 옵션 추가
  // 실제로 실행시에는 index.html 에서 모두 표현
  // URL에 해쉬 값이 들어가면 해당 URL 요청을 서버에 보내지 않고 브라우저 레벨에서 제어
  // 해쉬 값이 빠져 있으면 기본적으로 서버에서 요청을 받
  mode: 'history',
  routes: [
    {
      path: '/',
      redirect: '/login',
    },
    {
      path: '/login',
      // 다이나믹 importing
      // 코드 스플리팅 기법: 호출 시점에 필요한 페이지만 로딩, 처음 페이지 로딩 빠름
      component: () => import('@/views/LoginPage.vue'),
    },
    {
      path: '/signup',
      component: () => import('@/views/SignupPage.vue'),
    },
    {
      // 위에 없는 페이지일 경우 보여줄 페이지(사용자 친화적)
      path: '*',
      component: () => import('@/views/NotFoundPage.vue'),
    },
  ],
});
