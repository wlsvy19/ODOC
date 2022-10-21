import {
  // fetchNewsList,
  // fetchAskList,
  // fetchJobsList,
  fetchList,
  fetchUserInfo,
  fetchCommentItem
} from "../api/index.js";

export default {
  // FETCH_NEWS(context) {
  //   return fetchNewsList()
  //     .then(response => {
  //       // console.log(response.data);
  //       // commit을 해야 mutation접근 가능
  //       context.commit('SET_NEWS', response.data);
  //       return response;
  //     })
  //     .catch(error => {
  //       console.log('NEWS...ERROR', error);
  //     })
  // },
  // // DATA Destructuring, 위에코드 아래처럼 변경 가능
  // FETCH_ASK({ commit }) {
  //   return fetchAskList()
  //     .then(({ data }) => {
  //       commit('SET_ASK', data);
  //     })
  //     .catch(error => {
  //       console.log('ASK...ERROR', error);
  //     });
  // },
  // FETCH_JOBS({ commit }) {
  //   return fetchJobsList()
  //     .then(({ data }) => {commit('SET_JOBS', data);
  //     })
  //     .catch(error => {
  //       console.log('JOBS...ERROR', error);
  //     })
  // },

  // #2 실행
  // FETCH_LIST({ commit }, pageName) { // 라우터에 pageName값 넘어간다.
  //   // #3 함수 실행
  //   return fetchList(pageName) //#5 리턴
  //   .then(response => {
  //         // #4 커밋
  //     commit('SET_LIST', response.data);
  //     return response;
  //     })
  //     .catch(error => console.log(error));
  // },

  // async~await
  async FETCH_LIST({ commit }, pageName) { // 라우터에 pageName값 넘어간다.
    const response = await fetchList(pageName)
    commit('SET_LIST', response.data);
    return response; // return 해줘야 비동기 순서 보장됨, Promise() 리턴
  }, // -> api/index.js 

  FETCH_USER({ commit }, userName) {
    return fetchUserInfo(userName)
      .then(({ data }) => {
        commit('SET_USER', data);
      })
      .catch(error => {
        console.log('USER...ERROR', error)
      })
  },
  FETCH_ITEM({ commit }, id) {
    return fetchCommentItem(id)
      .then(({ data }) => {
        commit('SET_ITEM', data);
      })
      .catch(error => {
        console.log('ITEM...ERROR', error)
      })
  }
}