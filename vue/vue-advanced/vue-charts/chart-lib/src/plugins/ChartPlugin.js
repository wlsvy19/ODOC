// 플러그인: 인스턴스 생성 됐을 때 모든 컴포넌트에서 사용할 기능 정의
import Chart from 'chart.js/auto'


export default {
  install(Vue) {
    // $_ 다른곳에서 충돌나지 않게 네이밍, 다른 모든 곳에서 this.$_Chart로 접근 가능
    Vue.prototype.$_Chart = Chart;

    console.log(Vue, '차트 플러그인 로드됨')
  }
}