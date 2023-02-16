<template>
  <div>
    <!-- <app-header v-bind:프롭스 속성 이름 = "상위 컴포넌트의 데이터 이름"></app-header> -->
    <app-header v-bind:propsdata="str" v-on:reNew="renewString"> </app-header>
  </div>
</template>

<script>
import AppHeader from "./components/AppHeader.vue";

export default {
  // cli를 통해 생성된 .vue파일은 재사용성 가능성이 높음, 여러개 컴포넌트에서
  // 참조(공유)하면 안되기 때문에 함수를 쓰고 return형태로 사용 하도록 강제함
  data: function () {
    return {
      // return 으로 새로운 객체를 반환 하는 형태
      str: "Header-App.vue(상위컴포넌트)",
    };
  },
  components: {
    "app-header": AppHeader,
  },
  methods: {
    renewString: function () {
      this.str = "하위 컴포넌트 이벤트 전달 됨";
    },
  },
};
</script>

<style></style>
