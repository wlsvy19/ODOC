import axios from 'axios';

// 1. HTTP Request & Response와 관련된 기본 설정
const config = {
  baseUrl: 'https://api.hnpwa.com/v0/'
}

// 2. API 함수 정리
// function fetchNewsList() {
//   // return axios.get(config.baseUrl + 'news/1.json')
//   // ES6 템플릿 스트링 문법: 백틱 사용
//   return axios.get(`${config.baseUrl}news/1.json`)
// }

// function fetchAskList() {
//   return axios.get(`${config.baseUrl}ask/1.json`)
// }

// function fetchJobsList() {
//   return axios.get(`${config.baseUrl}jobs/1.json`)
// }
 
async function fetchList(pageName) {
  console.log(pageName + 'List...', '[api/index.js]에서 처리')
  // axios 리턴값: new Promise() -> .then().catch() 
  try {
    const response = await axios.get(`${config.baseUrl}${pageName}/1.json`);
    return response;
  } catch(error) {
    alert('List axios 요청 에러!!',  error);
  }
}

function fetchUserInfo(userName) {
  return axios.get(`${config.baseUrl}user/${userName}.json`)
}

function fetchCommentItem(id) {
  return axios.get(`${config.baseUrl}item/${id}.json`)
}


// ES6 모듈 문법: export 해야 다른데서 import 사용 가능
//export { fetchNewsList, fetchAskList, fetchJobsList, fetchUserInfo, fetchCommentItem,  }
export { fetchList, fetchUserInfo, fetchCommentItem, }

