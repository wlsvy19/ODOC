import Vue from 'vue';
import App from './App.vue';

// router 사용
import router from '@/routes/index';

Vue.config.productionTip = false;

new Vue({
  router,
  render: h => h(App),
}).$mount('#app');
