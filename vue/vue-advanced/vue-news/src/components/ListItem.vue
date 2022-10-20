<template>
  <ul class="news-list">
    <li v-for="news in listItems" :key="news.id" class="post">
      <div class="points">
        {{ news.points || 0 }}
      </div>
      <div>
        <p class="news-title">
          <template v-if="news.domain">
            <a :href="news.url">{{ news.title }}</a><small class="link-text" v-if="news.domain">({{ news.domain }})</small>
          </template>
          <template v-else>
            <router-link :to="`/item/${news.id}`">{{ news.title }}</router-link><small><a class="link-text" :href="news.domain" v-if="news.domain">({{ news.domain }})</a></small>
          </template>
        </p>
        <small v-if="news.user" class="link-text">
          by
          <router-link :to="`/user/${news.user}`" class="link-text">{{ news.user }}</router-link>
        </small>
        <small v-if="news.time_ago" class="link-text">
          {{ news.time_ago }}
        </small>
      </div>
    </li>
  </ul>
</template>

<script>
export default {
  created() {
    // const name = this.$route.name;

    // if (name === "news") {
    //   this.$store.dispatch("FETCH_NEWS");
    // } else if (name === "ask") {
    //   this.$store.dispatch("FETCH_ASK");
    // } else if (name === "jobs") {
    //   this.$store.dispatch("FETCH_JOBS");
    // }
  },
  computed: {
    // eslint-disable-next-line vue/return-in-computed-property
    listItems() {
      return this.$store.state.list;

      // const name = this.$route.name;
      // if (name === "news") {
      //   return this.$store.state.news;
      // } else if (name === "ask") {
      //   return this.$store.state.ask_items;
      // } else if (name === "jobs") {
      //   return this.$store.state.jobs;
      // }
    },
  },
};
</script>

<style scoped>
.news-list {
  margin: 0;
  padding: 0;
}
.post {
  list-style: none;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #eee;
}
.points {
  width: 80px;
  height: 60px;
  display: flex;
  /* 수평 정렬 */
  align-items: center;
  /* 수직 정렬 */
  justify-content: center;
  /* vue logo hex color */
  color: #42b883;
}
.news-title {
  margin: 0;
}
.link-text {
  color: #828282;
}
</style>