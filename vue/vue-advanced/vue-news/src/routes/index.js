import Vue from 'vue'
import VueRouter from 'vue-router'
import NewsView from '../views/NewsView.vue'
import AskView from '../views/AskView.vue'
import JobsView from '../views/JobsView.vue'
import UserView from '../views/UserView.vue'
import ItemView from '../views/ItemView.vue'


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
            // name: 분기처리에 사용
            name: 'news',
            // component: url주소로 갔을 때 표시 될 컴포넌트
            component: NewsView,
        },
        {
            path: '/ask',
            name: 'ask',
            component: AskView
        },
        {
            path: '/jobs',
            name: 'jobs',
            component: JobsView
        },
        {
            path: '/user/:id', // route의 params를 받음
            component: UserView
        },
        {
            path: '/item/:id',
            component: ItemView
        }

    ]
})