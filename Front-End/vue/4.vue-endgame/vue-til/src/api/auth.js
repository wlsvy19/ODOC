// 로그인, 회원가입

import { instance } from './index';

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

export { registerUser, loginUser };
