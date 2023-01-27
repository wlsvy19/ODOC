import Vue from 'vue';
import VueRouter from 'vue-router';
// jsconfig.json 파일에서 별칭 설정
// 코드 스플리팅으로 뺌
// import LoginPage from '@/views/LoginPage';
// import SignupPage from '@/views/SignupPage';

// 플러그인 초기화(사용)하기 위한 코드
Vue.use(VueRouter);

// 라우터 인스턴스 생성
// export: VueRouter 객체를 외부에서 사용하기 위함 -> main.js 에서 사용
export default new VueRouter({
  // URL # 제거
  // 실제로 실행시 index.html 에서 모두 표현(SPA)
  // URL에 해쉬(#) 값이 들어가면 해당 URL 요청을 서버에 보내지 않고 브라우저 레벨에서 제어
  // 해쉬(#) 값이 빠져 있으면 기본적으로 서버에서 요청을 받기 때문에 라우터 히스토리 모드를 사용하게 되면 서버에도 fallback 옵션을 추가
  mode: 'history',

  // 페이지 정보를 담음
  routes: [
    {
      path: '/',
      // 초기 화면을 로그인 페이지로 설정
      redirect: '/login',
    },
    {
      path: '/login',
      // 다이나믹 importing
      // 코드 스플리팅 기법: 호출 시점에 필요한 페이지만 로딩, 처음 페이지 로딩 빠름(lazy)
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
