import Vue from 'vue';
import App from './App.vue';

// router 사용
import router from '@/routes/index';

// 상태관리를 위한 vuex의 store 사용
import store from '@/store/index';

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app');
