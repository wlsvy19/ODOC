import {
    fetchNewsList,
    fetchAskList,
    fetchJobsList,
    fetchUserInfo,
    fetchCommentItem
} from "../api/index.js";

export default {
    FETCH_NEWS(context) {
        fetchNewsList()
            .then(response => {
                // console.log(response.data);
                // commit을 해야 mutation접근 가능
                context.commit('SET_NEWS', response.data);
            })
            .catch(error => {
                console.log('NEWS...ERROR', error);
            })
    },
    // DATA Destructuring, 위에코드 아래처럼 변경 가능
    FETCH_ASK({ commit }) {
        fetchAskList()
            .then(({ data }) => {
                commit('SET_ASK', data);
            })
            .catch(error => {
                console.log('ASK...ERROR', error);
            });
    },
    FETCH_JOBS({ commit }) {
        fetchJobsList()
            .then(({ data }) => {
                commit('SET_JOBS', data);
            })
            .catch(error => {
                console.log('JOBS...ERROR', error);
            })
    },
    FETCH_USER({ commit }, userName) {
        fetchUserInfo(userName)
            .then(({ data }) => {
                commit('SET_USER', data);
            })
            .catch(error => {
                console.log('USER...ERROR', error)
            })
    },
    FETCH_ITEM({ commit }, id) {
        fetchCommentItem(id)
            .then(({ data }) => {
                commit('SET_ITEM', data);
            })
            .catch(error => {
                console.log('ITEM...ERROR', error)
            })
    }
}