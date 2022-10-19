<template>
  <div>
    <ListItem />
  </div>
</template>

<script>
// import { fetchJobsList } from "../api/index.js";
import ListItem from "../components/ListItem.vue";
export default {
  components: {
    ListItem
  }
  // data: function () {
  //   return {
  //     jobs: [],
  //   };
  // },
  //created: function () {
    // fetchJobsList()
    //   .then(response => this.jobs = response.data)
    //   .catch(error => alert(error));
    //this.$store.dispatch("FETCH_JOBS");
  //},
};
</script>

<style>
</style>