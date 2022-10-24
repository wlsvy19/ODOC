<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div>
        <!-- {{ propsdata }} -->
    <p1>item.vue todo something...</p1>
    <slot name = "first">
      <p>first</p>
      <!-- slot: 등록하는 곳에서 정의할 화면 영역-->
    </slot>
    <slot name = "second">
      <p> second</p>
    </slot>
  </div>


</template>
<!--슬롯: 상위컴포넌트들에서 하위 컴포넌트 사용중일때 특정 상위컴포넌트에서만 하위컴포넌트 재사용 하고 싶을 경우-->
<!--슬롯사용 이유1: 상위컴포넌트 에서 확장 가능함(기능추가 바로바로가능)-->
<!--슬롯사용 이유2: 슬롯 이름 지정해서 v-if~else 처럼 사용도 가능함(원하는 페이지에만 컴포넌트 보여주기)-->



<!-- <script>
export default {
  props: ["propsdata"],
};
</script> -->