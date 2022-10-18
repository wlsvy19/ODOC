<template>
  <div>
    <p>name: {{ userInfo.id }}</p>
    <p>karma(활동점수): {{ userInfo.karma }}</p>
    <p>created: {{ userInfo.created }}</p>
  </div>
</template>

<script>
export default {
  computed: {
    // template은 최대한 정돈되도록 코딩
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

<style>
</style>