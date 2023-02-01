<template>
  <li>
    <div class="post-title">{{ postItem.title }}</div>
    <div class="post-contents">{{ postItem.contents }}</div>
    <div class="post-time">
      {{ postItem.createdAt }}
      <i class="icon ion-md-create" @click="routeEditPage"></i>
      <i class="icon ion-md-trash" @click="deleteItem"></i>
    </div>
  </li>
</template>

<script>
import { deletePost } from '@/api/posts';
import bus from '@/utils/bus.js';

export default {
  props: {
    postItem: {
      type: Object,
      required: true,
    },
  },
  methods: {
    async deleteItem() {
      // confirm(): 확인-true, 취소-false
      if (confirm('정말 게시물을 삭제하시겠습니까?')) {
        console.log('삭제 게시물 ID: ', this.postItem._id);
        await deletePost(this.postItem._id);
        bus.$emit('show:toast', `성공적으로 삭제되었습니다.`);

        // 삭제 후, 삭제된 게시물 리스트 봐야 함
        // 하위 -> 상위 이벤트 전달, MainPage.vue의 fetchData() 호출
        this.$emit('refresh');
      }
    },

    // 수정 버튼 클릭시 특정 페이지 이동
    routeEditPage() {
      console.log('수정 아이콘 클릭');
      // 다이나믹 라우트 매칭: route.index.js 에서 path에 '/post/:id'  사용
      const id = this.postItem._id;
      this.$router.push(`/post/${id}`);
    },
  },
};
</script>

<style></style>
