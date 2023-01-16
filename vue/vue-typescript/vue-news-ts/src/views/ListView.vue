<template>
  <div>
    <ListItem :items="newsItems"></ListItem>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { fetchNews, NewsItem } from "@/api";
import ListItem from "../components/ListItem.vue";

export default Vue.extend({
  components: {
    ListItem,
  },
  data() {
    return {
      newsItems: [] as NewsItem[],
    };
  },

  methods: {
    async fetchNewsItems() {
      const response = await fetchNews();
      console.log(response);
      this.newsItems = response.data; // 여기서 데이터 지정
    },
  },
  created() {
    this.fetchNewsItems();
  },
});
</script>

<style></style>
