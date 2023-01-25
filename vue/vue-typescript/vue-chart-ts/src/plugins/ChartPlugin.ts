import Chart from "chart.js";
import { VueConstructor } from "vue";

export default {
  install(Vue: VueConstructor) {
    Vue.prototype.$_Chart = Chart;
  },
};
