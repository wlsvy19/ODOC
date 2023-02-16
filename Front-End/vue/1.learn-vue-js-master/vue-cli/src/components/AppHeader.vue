<template>
  <header>
    <p1>{{ propsdata }}</p1>
    <br />
    <p1>{{ str }}</p1>
    <br />
    <button v-on:click="sendEvent">Send</button>
  </header>
</template>

<script>
export default {
  // 하위 컴포넌트에서 사용, 상위 컴포넌트 데이터 받을 때 씀.
  props: ["propsdata"],
  data: function () {
    return {
      str: "Header-AppHeader.vue(하위컴포넌트)",
    };
  },
  methods: {
    sendEvent: function () {
      this.$emit("reNew"); // emit 날리는 순간 상위 컴포넌트의 태그에서 v-on으로 받음
    },
  },
};
</script>

<style></style>
