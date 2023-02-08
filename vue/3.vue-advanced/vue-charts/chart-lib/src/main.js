import Vue from 'vue'
import App from './App.vue'
import ChartPlugIn from './plugins/ChartPlugin.js'

Vue.config.productionTip = false

// ChartPlugIn의 속성함수 install() 실행
Vue.use(ChartPlugIn)

new Vue({
  render: h => h(App),
}).$mount('#app')
