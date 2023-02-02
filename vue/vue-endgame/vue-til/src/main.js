import Vue from 'vue';
import App from './App.vue';

// router 사용
import router from '@/routes/index';

// 상태관리를 위한 vuex의 store 사용
import store from '@/store/index';

// 날짜 형식 포맷을 위한 필터
import { formatDate } from '@/utils/filters';

Vue.config.productionTip = false;

// 날짜 필터 전역 사용
Vue.filter('formatDate', formatDate);

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app');
