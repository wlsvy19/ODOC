// 하이오더 컴포넌트

import ListView from "./ListView.vue";
import bus from "../utils/bus";

export default function createListView(name) {
  return {
    // 재사용할 인스턴스(컴포넌트) 옵션들이 들어갈 자리
    name: name,
    created() {
      bus.$emit("start:spinner"); // create되면 바로 스피너 호출
      setTimeout(() => {
        this.$store
          .dispatch("FETCH_LIST", this.$route.name) // 데이터 요청
          .then(() => {
            // actions.js에 fetchNewsList반환값이 promise라서 then 게속 사용 가능
            bus.$emit("end:spinner");
          })
          .catch((error) => {
            console.log(error);
          });
      }, 2000); // 타임아웃 걸어서 3초뒤에 받아옴
    },
    render(createElemnet) {
      return createElemnet(ListView);
    },
  };
}
