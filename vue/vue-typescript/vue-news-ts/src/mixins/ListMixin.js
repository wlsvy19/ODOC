// 재사용!! -> mixin

import bus from "../utils/bus";

export default {
  // 재사용할 컴포넌트 옵션 -> 라우터 네비게이션 으로 대체
  // created() {
  //   bus.$emit("start:spinner");

  //   // 1. 데이터 호출 시점-> actions 호출
  //   this.$store.dispatch("FETCH_LIST", this.$route.name)
  //     .then(() => {
  //       bus.$emit("end:spinner");
  //     })
  //     .catch((error) => {
  //       console.log(error);
  //     });
  // }

  mounted() {
    // 데이터가 다 불러와질때까지 스피너 돌고 데이터 나오자마자 사라짐
    bus.$emit("end:spinner");
  },
};
