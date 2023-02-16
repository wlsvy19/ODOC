<template>
  <div>
    <!-- ref: vue에서 제공 하는 속성-->
    <canvas id="myChart" ref="myChart"></canvas>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { ChartConfiguration } from "chart.js";
import { VueConstructor } from "vue/types/umd";
import { MyVueRefs } from "./types/refs";

// export default (
//   Vue as VueConstructor<Vue & { $refs: { myChart: HTMLCanvasElement } }>
// ).extend({

// 반복되는 ref를 줄이기 위해 ref.ts 파일을 만든 후 사용
export default (Vue as MyVueRefs<{ myChart: HTMLCanvasElement }>).extend({
  methods: {
    sayHi() {
      this.$refs.my;
    },
  },
  mounted() {
    const labels = ["January", "February", "March", "April", "May", "June"];
    const data = {
      labels: labels,
      datasets: [
        {
          label: "My First dataset",
          backgroundColor: "rgb(255, 99, 132)",
          borderColor: "rgb(255, 99, 132)",
          data: [0, 10, 5, 2, 20, 30, 45],
        },
      ],
    };
    const config: ChartConfiguration = {
      type: "line",
      data,
      options: {},
    };
    // document.getElementById -> 전역 접근 . . . 별로임
    // const ctx = (
    //   document.getElementById("myChart") as HTMLCanvasElement
    // ).getContext("2d") as CanvasRenderingContext2D;

    /**
     * ref: 뷰에서 특정 DOM이나 컴포넌트 정보에 접근하기 위해 사용, 컴포넌트 내 특정 DOM에 접근할 때 사용
     */
    const canvasElement = this.$refs.myChart;
    const ctx = canvasElement.getContext("2d") as CanvasRenderingContext2D;
    /**
     * ! 차트를 직접 플러그인 화 하여 사용 해보기
     */
    var myChart = new this.$_Chart(ctx, config);
  },
});
</script>
