<template>
  <div>
    <h1>Chart.js</h1>
    <bar-chart v-on:hover="hoverChart" v-bind:propsdata="barChartDataSet" />
    <line-chart></line-chart>
  </div>
</template>

<script>
import BarChart from "./components/BarChart.vue";
import LineChart from "./components/LineChart.vue";

export default {
  // 컴포넌트 속성 && 인스턴스 옵션
  components: {
    BarChart,
    LineChart,
  },
  data() {
    return {
      barChartDataSet: [{
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
          }],
    }
  },
  methods: {
    //3초뒤에 차트 그려주는 로직 뿌려주기 가능
    // getChartData() {
    //   setTimeout(()=>{
    //     this.chartDataSet = [{
    //         label: "Bar Chart",
    //         data: [15, 19, 3, 5, 2, 3],
    //         backgroundColor: [
    //           "rgba(255, 99, 132, 0.2)",
    //           "rgba(54, 162, 235, 0.2)",
    //           "rgba(255, 206, 86, 0.2)",
    //           "rgba(75, 192, 192, 0.2)",
    //           "rgba(153, 102, 255, 0.2)",
    //           "rgba(255, 159, 64, 0.2)",
    //         ],
    //         borderColor: [
    //           "rgba(255, 99, 132, 1)",
    //           "rgba(54, 162, 235, 1)",
    //           "rgba(255, 206, 86, 1)",
    //           "rgba(75, 192, 192, 1)",
    //           "rgba(153, 102, 255, 1)",
    //           "rgba(255, 159, 64, 1)",
    //         ],
    //         borderWidth: 1,
    //       }]
    //   }, 1000);
    // },
      hoverChart: function() {
        console.log('차트 호버 이벤트 발생')
      }
  },
  // 등록한 컴포넌트에서 데이터 호출 할때 created 라이프사이클 훅 사용
  created() {
    // axios.get('요청URL')
    // .then(response => this.chartDataSet = response.data)
    // .catch(error => console.log(error))
  }
};
</script>

<style>
</style>