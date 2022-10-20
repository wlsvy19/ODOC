<template>
  <div id="app">
    <!-- 파스칼->케밥 -->
    <tool-bar></tool-bar>
    <transition name="routing-fade">
      <router-view></router-view>
    </transition>
    <!-- 
    <NewsView></NewsView>
    <AskView></AskView>
    <JobsView></JobsView> 
    -->
    <!-- router/index.js 파일로 이동 -->
    <!-- <router-view></router-view> -->
  </div>
</template>

<script>
import ToolBar from "./components/ToolBar.vue";

// import JobsView from "./views/JobsView.vue";
// import NewsView from "./views/NewsView.vue";
// import AskView from "./views/AskView.vue";

export default {
  components: {
    ToolBar,
    // JobsView,
    // NewsView,
    // AskView,
  },
};
</script>

<style>
body{
  padding: 0;
  margin: 0; 
}
a {
  /* 앵커태그 밑줄 */
  text-decoration: none;
  color: #34495e;

}
/* 앵커태그 커서 올려놨을 때 */
a:hover {
  color: #42b883;
  text-decoration: underline;
}
/* 앵커태그 선택한곳 밑줄 그어짐 */
a.router-link-exact-active {
  text-decoration: underline;
}
/* Router Transition */
.routing-fade-enter-active, .routing-fade-leave-active {
  transition: opacity 0.3s ease;
}
.routing-fade-enter, .routing-fade-leave-to
/* .routing-fade-leave-active below version 2.1.8 */ {
  opacity: 0;
}
</style>