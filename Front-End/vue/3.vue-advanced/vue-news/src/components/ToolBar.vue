<template>
  <div class="header">
    <router-link to="/news">News</router-link> |
    <router-link to="/ask">Ask</router-link> |
    <router-link to="/jobs">Jobs</router-link> |
  </div>
</template>

<style scoped>
.header {
  color: white;
  background-color: #42b883;
  display: flex;
  padding: 8px;
}

/* 툴바 클릭 했을 때 구분 */
.header .router-link-exact-active {
  color: #35495e;
}
.header a {
    color: white;
}
</style>