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
/* Router Transition */
.routing-fade-enter-active, .routing-fade-leave-active {
  transition: opacity 0.3s ease;
}
.routing-fade-enter, .routing-fade-leave-to
/* .routing-fade-leave-active below version 2.1.8 */ {
  opacity: 0;
}
</style>