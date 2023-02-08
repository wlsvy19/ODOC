<template>
  <div>
    <section>
      <!--  사용자 정보 영역-->
      <user-profile :info="fetchedItem">
        <router-link slot="username" :to="`/user/${fetchedItem.user}`">
          {{fetchedItem.user}}
        </router-link>
        <template slot="time">Posted {{fetchedItem.time_ago}}</template>
      </user-profile>
    </section>

    <section>
      <h2>{{ fetchedItem.title }}</h2>
      <!-- 질문의 댓글 영역, v-html 디렉티브: <p>태그등 HTML태그 인식 시킴 -->
      <div v-html="fetchedItem.content"></div>
    </section>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import UserProfile from "../components/UserProfile.vue";

export default {
  components: {
    UserProfile
  },
  computed: {
    ...mapGetters(["fetchedItem"]),
  },
  created() {
    const itemId = this.$route.params.id;
    console.log(itemId);
    this.$store.dispatch("FETCH_ITEM", itemId);
  },
};
</script>

<style scoped>
.user-container {
  display: flex;
  align-items: center;
  padding: 0.6rem;
}
.fa-user {
  font-size: 2.5rem;
}
.user-description {
  padding-left: 8px;
}
.time {
  font-size: 0.7rem;
}
</style>