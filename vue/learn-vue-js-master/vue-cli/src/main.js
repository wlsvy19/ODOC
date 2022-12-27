import Vue from "vue";
import App from "./App.vue";

Vue.config.productionTip = false;

new Vue({
  render: (h) => h(App),
}).$mount("#app");



/*
var App = {
  template: '<div>content...</div>'
}

new Vue({
  el: '#app',
  render: h => h(App), // render: vue 내부에서 template 속성 정의시 실행되는 함수
  components: {
  'app': App
  }
})
*/
