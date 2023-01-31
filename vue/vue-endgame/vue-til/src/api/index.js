// API 가 들어있는 파일

import axios from 'axios';

import { setInterceptors } from '@/api/common/interceptors';

// axios 객체 생성 함수
function createInstance() {
  // axios 관련 공통 코드
  const instance = axios.create({
    // ! .env.development 파일을 가리킴 !
    // baseURL: axios.create에서 기본제공 되는 속성
    baseURL: process.env.VUE_APP_API_URL,

    // Authorization: HTTP헤더에 정의되어 있는 스펙, 토큰을 담아줌
    // headers: {
    //   Authorization: store.state.token,
    // },
  });
  // 인터셉터 함수 안에 axios 들어감
  return setInterceptors(instance);
}

const instance = createInstance();

// 회원가입 할 때 날리는 요청 API
function registerUser(userData) {
  // const url = 'http://localhost:3000/signup';
  // return axios.post(url, userData);

  // http://localhost:3000/signup  으로 데이터 보냄
  return instance.post('signup', userData);
}

// 로그인 할 때 날리는 요청 API
function loginUser(userData) {
  return instance.post('login', userData);
}

// 학습 노트 데이터를 조회하는 API
function fetchPosts() {
  return instance.get('posts');
}

export { registerUser, loginUser, fetchPosts };
