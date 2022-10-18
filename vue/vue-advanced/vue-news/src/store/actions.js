import { fetchNewsList, fetchAskList, fetchJobsList } from "../api/index.js";

export default {
    FETCH_NEWS(context) {
        fetchNewsList()
            .then(response => {
                console.log(response.data);
                // commit을 해야 mutation접근 가능
                context.commit('SET_NEWS', response.data);
            })
            .catch(error => {
                console.log('NEWS...ERROR', error);
            })
    },
    // DATA Destructuring
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
    }
}