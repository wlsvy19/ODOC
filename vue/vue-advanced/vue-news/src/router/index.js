import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const router = new VueRouter({
    routes: [
        {
            //path: url 주소
            path: '',
            // component: url주소로 갔을 때 표시 될 컴포넌트
            component: '',
        },
        {
            path: '',
            component: ''
        },
        {
            path: '',
            component: ''
        },
    ]
})

new Vue({
    router
})