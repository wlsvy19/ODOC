<template>
  <div>
    <h1>Jobs...</h1>
    <div v-for="job in jobs">* {{ job.title }}</div>
  </div>
</template>

<script>
import { fetchJobsList } from "../api/index.js";
export default {
  data: function () {
    return {
      jobs: [],
    };
  },
  created: function () {
    fetchJobsList()
      .then(response => this.jobs = response.data)
      .catch(error => alert(error));
  },
};
</script>

<style>
</style>