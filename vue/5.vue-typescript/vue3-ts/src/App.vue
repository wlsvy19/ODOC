<template>
  <img alt="Vue logo" src="./assets/logo.png" />
  <HelloWorld msg="Welcome to Your Vue.js + TypeScript App" />
  <div>{{ str }}</div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import HelloWorld from "./components/HelloWorld.vue";

// export default Vue.extend({})  -> defineComponent

export default defineComponent({
  name: "App",
  components: {
    HelloWorld,
  },
  // mixins 문제점 -> setup API 사용
  // 아래 data()  + methods -> setup()
  // setup() {
  //   const str = ref("");
  //   str.value = "hello";

  //   return { str };
  // },

  data() {
    return {
      str: "hi hi hi hi",
    };
  },
  methods: {
    changeStr() {
      this.str = "!";
      // this.str = 10;
    },
  },
});
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
