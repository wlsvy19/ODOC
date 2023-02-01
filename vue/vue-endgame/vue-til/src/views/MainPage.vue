<template>
  <div>
    <div class="main list-container contents">
      <h1 class="page-header">Today I Learned</h1>

      <LoadingSpinner v-if="isLoading"></LoadingSpinner>

      <ul v-else>
        <!-- PostListItem.vue 컴포넌트로 뺌 -->
        <PostListItem
          v-for="postItem in postItems"
          v-bind:key="postItem._id"
          v-bind:postItem="postItem"
          @refresh="fetchData"
        ></PostListItem>
      </ul>
    </div>
    <router-link to="/add" class="create-button">
      <i class="ion-md-add"></i>
    </router-link>
  </div>
</template>

<script>
import PostListItem from '@/components/posts/PostListItem.vue';
import LoadingSpinner from '@/components/common/LoadingSpinner.vue';
import { fetchAllPost } from '../api/posts';
export default {
  components: {
    PostListItem,
    LoadingSpinner,
  },
  data() {
    return {
      postItems: [],
      isLoading: false,
    };
  },
  methods: {
    // 게시물 가져오는 API
    async fetchData() {
      // 데이터 불러오는 동안 로딩창
      this.isLoading = true;

      const { data } = await fetchAllPost();
      // 데이터 다 받아오면 로딩창 안보이게
      this.isLoading = false;

      console.log('게시물: ', data.posts);
      this.postItems = data.posts;
    },
  },
  created() {
    this.fetchData();
  },
};
</script>

<style></style>
