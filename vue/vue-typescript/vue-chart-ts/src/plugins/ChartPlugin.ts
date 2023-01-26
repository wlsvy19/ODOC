import Chart from "chart.js/auto";
import { VueConstructor } from "vue/types/umd";

export default {
  install(Vue: VueConstructor) {
    Vue.prototype.$_Chart = Chart;
  },
};
