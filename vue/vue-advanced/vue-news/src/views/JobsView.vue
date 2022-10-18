<template>
  <div>
    <p v-for="item in this.$store.state.jobs">
      <a v-bind:href="item.url">* {{ item.title }}</a>
      <small> {{item.time_ago}}, {{item.domain}}</small>
    </p>
  </div>
</template>

<script>
// import { fetchJobsList } from "../api/index.js";
export default {
  // data: function () {
  //   return {
  //     jobs: [],
  //   };
  // },
  created: function () {
    // fetchJobsList()
    //   .then(response => this.jobs = response.data)
    //   .catch(error => alert(error));
    this.$store.dispatch('FETCH_JOBS')
  },
};
</script>

<style>
</style>