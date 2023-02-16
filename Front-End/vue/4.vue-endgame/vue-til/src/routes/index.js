import Vue from 'vue';
import VueRouter from 'vue-router';
// jsconfig.json 파일에서 별칭 설정
// 코드 스플리팅으로 뺌
// import LoginPage from '@/views/LoginPage';
// import SignupPage from '@/views/SignupPage';

// 스토어dp 저장된 로그인 유저 정보 가져오기
import store from '@/store/index';

// 플러그인 초기화(사용)하기 위한 코드
Vue.use(VueRouter);

// 라우터 인스턴스 생성
// export: VueRouter 객체를 외부에서 사용하기 위함 -> main.js 에서 사용
const router = new VueRouter({
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
      // 코드 스플리팅 기법: 미리 다 임포트 x -> 호출 시점에 필요한 페이지만 임포트해서 로딩, 처음 페이지 로딩 빠름(lazy)
      component: () => import('@/views/LoginPage.vue'),
    },
    {
      path: '/signup',
      component: () => import('@/views/SignupPage.vue'),
    },
    {
      path: '/main',
      component: () => import('@/views/MainPage.vue'),
      // 토큰을 가지고 있어야(권한) 접근 가능함
      meta: { auth: true },
    },
    {
      path: '/add',
      component: () => import('@/views/PostAddPage.vue'),
      // 토큰을 가지고 있어야(권한) 게시물 쓸 수 있음
      meta: { auth: true },
    },
    {
      // 다이나믹 라우팅
      path: '/post/:id',
      component: () => import('@/views/PostEditPage.vue'),
      // 토큰을 가지고 있어야(권한) 게시물 수정 가능
      meta: { auth: true },
    },
    {
      // 위에 없는 페이지일 경우 보여줄 페이지(사용자 친화적)
      path: '*',
      component: () => import('@/views/NotFoundPage.vue'),
    },
  ],
});

// 라우터 네비게이션 가드: 페이지 이동시 특정 데이터가 있어야만(권한 등) 이동 가능하도록 설정
// to: 이동하려는 페이지
// from: 현재 머물고 있는 페이지
// next: to 페이지 이동시 호출하는 API
router.beforeEach((to, from, next) => {
  // 1. 인증이 필요한 페이지 이고 사용자가 로그인 하지 않았을 경우
  if (to.meta.auth && !store.getters.isLogin) {
    alert('로그인이 필요한 서비스 입니다.');

    // 2. 로그인 하라고 보냄
    next('/login');

    // 로그인 하고 나서 if 밖의 next() 호출 하지 않기 위해 return 사용
    return;
  }
  // next를 호출 해야만 다음페이지 넘어갈 수 있음
  next();
});

export default router;
