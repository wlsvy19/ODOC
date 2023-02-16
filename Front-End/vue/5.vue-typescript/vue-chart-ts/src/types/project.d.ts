// tsconfig.json -> include에 인식 하도록 추가

import Vue from "vue";
import Chart = require("chart.js");

declare module "vue/types/vue" {
  interface Vue {
    $_Chart: Chart;
    // 라이브러리: any;
  }
}

// npm i @type/라이브러리 이름 -> 안되면
// @types 라이브러리가 제공되지 않는 라이브러리의 경우
// declare module "라이브러리 이름";
