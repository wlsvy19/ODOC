<template>
  <div>
    <canvas ref="lineChart" id="lineChart"></canvas>
  </div>
</template>

<script>
// 플러그인 만들어서 뺌
//import Chart from "chart.js/auto"; 

export default {
  mounted() {
    //var ctx = document.getElementById("lineChart").getContext("2d");

    // vue에서는 ref로 DOM에 접근
    //var ctx = this.$refs.lineChart.getContext("2d");
    
    var chart = new this.$_Chart(this.$refs.lineChart.getContext("2d"), {
      type: "line",
      data: {
        labels: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월"],
        datasets: [
          {label: "Line Chart",
            backgroundColor: "rgb(255, 99, 132)",
            borderColor: "rgb(255, 99, 132)",
            data: [0, 10, 5, 2, 20, 30, 45, 60],
          }],
      },
      options: {},
    });
    console.log(chart);
  },
};
</script>

<style>
</style>