import Vue from 'vue'
import VueRouter from 'vue-router'
import NewsView from '../views/NewsView.vue'
import AskView from '../views/AskView.vue'
import JobsView from '../views/JobsView.vue'

Vue.use(VueRouter)
export const router = new VueRouter({
    // URL에 #(해쉬)값 제거
    mode: 'history',
    routes: [ //URL 라우팅 정보
        {
            path: '/',
            redirect: '/news' // 홈페이지 접속하자마자 news페이지 이동
        },
        {
            //path: url 주소
            path: '/news',
            // component: url주소로 갔을 때 표시 될 컴포넌트
            component: NewsView,
        },
        {
            path: '/ask',
            component: AskView
        },
        {
            path: '/jobs',
            component: JobsView
        }
    ]
})