<template>
  <div>
    <user-profile :info="userInfo">
      <div slot="username">ID: {{ userInfo.id }}</div>
      <span slot="time">{{ "Joined " + userInfo.created }}, </span>
      <span slot="karma">{{ userInfo.karma }}</span>
    </user-profile>
  </div>
</template>

<script>
import UserProfile from "../components/UserProfile.vue";
export default {
  components: {
    UserProfile,
  },
  computed: {
    userInfo() {
      return this.$store.state.user;
    },
  },
  created() {
    const userName = this.$route.params.id;
    //axios ->store 에서 처리
    this.$store.dispatch("FETCH_USER", userName);
  },
};
</script>

<style></style>
