// API 가 들어있는 파일

import axios from 'axios';

import { setInterceptors } from '@/api/common/interceptors';

// axios 객체 생성 및 초기화 함수

// 로그인전 권한이 필요없는 경우 - 단순 URL만 생성
function createInstance() {
  return axios.create({
    // ! .env.development 파일을 가리킴 !
    // baseURL: axios.create에서 기본제공 되는 속성
    baseURL: process.env.VUE_APP_API_URL,

    // Authorization: HTTP헤더에 정의되어 있는 스펙, 토큰을 담아줌
    // headers: {
    //   Authorization: store.state.token,
    // },
  });
}

// 로그인 이후 권한이 필요한 경우
function createInstanceWithAuth(url) {
  const instance = axios.create({
    baseURL: `${process.env.VUE_APP_API_URL}${url}`,
  });
  // 인터셉터 함수 안에 axios 들어감
  return setInterceptors(instance);
}

export const instance = createInstance();

// RESTful 한 API 설계 - HTTP 메서드만 다르고 기본URL은 같도록 설계
// GET - posts
// POST - posts
// PUT - posts/{id}
// FETCH - posts/{id}
export const posts = createInstanceWithAuth('posts');
