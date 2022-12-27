// API 가 들어있는 파일

import axios from 'axios';

const instance = axios.create({
  // .env.development 파일을 가리킴
  baseURL: process.env.VUE_APP_API_URL,
});

// 회원가입 할 때 날리는 요청
function registerUser(userData) {
  // const url = 'http://localhost:3000/signup';
  // return axios.post(url, userData);

  return instance.post('signup', userData);
}

// 로그인 할 때 날리는 요청
function loginUser(userData) {
  return instance.post('login', userData);
}

export { registerUser, loginUser };
