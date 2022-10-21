<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <li>
    <!-- {{ propsdata }} -->

    <slot>
      <!-- slot: 등록하는 곳에서 정의할 화면 영역-->
    </slot>
  </li>
</template>

<!--슬롯사용 이유1: 상위컴포넌트 에서 확장 가능함(기능추가 바로바로가능)-->
<!--슬롯사용 이유2: 슬롯 이름 지정해서 v-if~else 처럼 사용도 가능함(원하는 페이지에만 컴포넌트 보여주기)-->



<!-- <script>
export default {
  props: ["propsdata"],
};
</script> -->