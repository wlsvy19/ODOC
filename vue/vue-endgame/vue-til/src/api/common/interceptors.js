/**
 * * 인터셉터 사용이유: 로그인할 시점에 state의 token값은 ''로 되어있음 ->loginUser 함수 호출 시점에 '' 이므로 토큰을 가로채서 미리 만들어둠
 */

import store from '@/store/index';

export function setInterceptors(instance) {
  // 요청 인터셉터 추가하기
  instance.interceptors.request.use(
    function(config) {
      // 요청이 전달되기 전에 작업 수행
      // console.log('인터셉어 request의 config: ', config);
      config.headers.Authorization = store.state.token;
      return config;
    },
    function(error) {
      // 요청 오류가 있는 작업 수행
      return Promise.reject(error);
    },
  );

  // 응답 인터셉터 추가하기
  instance.interceptors.response.use(
    function(response) {
      // 2xx 범위에 있는 상태 코드는 이 함수를 트리거 합니다.
      // 응답 데이터가 있는 작업 수행
      return response;
    },
    function(error) {
      // 2xx 외의 범위에 있는 상태 코드는 이 함수를 트리거 합니다.
      // 응답 오류가 있는 작업 수행
      return Promise.reject(error);
    },
  );
  return instance;
}
