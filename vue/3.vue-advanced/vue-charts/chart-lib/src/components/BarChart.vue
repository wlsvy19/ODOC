<template>
  <canvas ref="barChart" id="barChart" width="400" height="400"></canvas>
</template>

<script>
// 플러그인 만들어서 뺌
//import Chart from "chart.js/auto";

export default {
  // 상위 컴포넌트 데이터 받을 때 사용, 상위에선 v-bind:propsdata
  props: ["propsdata"],
  mounted() {
    // document.getElementById는 컴포넌트가 생성 되서 붙어야 하기때문에 mounted안에 넣어야함
    //const ctx = document.getElementById("barChart");

    // vue에서는 ref로 DOM에 접근
    // 컴포넌트 기반에서는 각각 템플릿에서만 접근할 수 있는 ref 사용하는게 좋음
    //var ctx = this.$refs.barChart.getContext("2d");

    var myChart = new this.$_Chart(this.$refs.barChart.getContext("2d"), {
      type: "bar",
      data: {
        labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],
        datasets: this.propsdata,
        /*
        [
           
          {
            label: "Bar Chart",
            data: [15, 19, 3, 5, 2, 3],
            backgroundColor: [
              "rgba(255, 99, 132, 0.2)",
              "rgba(54, 162, 235, 0.2)",
              "rgba(255, 206, 86, 0.2)",
              "rgba(75, 192, 192, 0.2)",
              "rgba(153, 102, 255, 0.2)",
              "rgba(255, 159, 64, 0.2)",
            ],
            borderColor: [
              "rgba(255, 99, 132, 1)",
              "rgba(54, 162, 235, 1)",
              "rgba(255, 206, 86, 1)",
              "rgba(75, 192, 192, 1)",
              "rgba(153, 102, 255, 1)",
              "rgba(255, 159, 64, 1)",
            ],
            borderWidth: 1,
          },
          
        ],
        */
      },

      options: {
        onClick: function () {
          //console.log('호버')
          this.$emit("hover");
        },
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      },
    });
    console.log(myChart);
  },
};
</script>

<style>
</style>