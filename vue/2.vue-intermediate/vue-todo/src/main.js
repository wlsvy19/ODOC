import Vue from 'vue'
import App from './App.vue'
import {store} from './store/store.js' //store는 const 변수이기 때문에 {}

Vue.config.productionTip = false

new Vue({
  store,
  render: h => h(App),
}).$mount('#app')

// new Vue({
//   el: '#app',
//   store,
//   render: h=> h(App)
// })
