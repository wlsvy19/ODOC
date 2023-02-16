<template>
  <div class="contents">
    <h1 class="page-header">Edit Post</h1>
    <div class="form-wrapper">
      <form class="form" @submit.prevent="submitForm">
        <div>
          <label for="title">Title:</label>
          <input id="title" type="text" v-model="title" />
        </div>
        <div>
          <label for="contents">Contents:</label>
          <textarea
            id="contents"
            type="text"
            rows="5"
            v-model="contents"
          ></textarea>
          <p v-if="!isContentsValid" class="validation-text warning">
            게시물의 내용은 200글자 이하여야 합니다.
          </p>
        </div>
        <button type="submit" class="btn">Edit</button>
      </form>
      <p class="log">
        {{ logMessage }}
      </p>
    </div>
  </div>
</template>

<script>
import { fetchOnePost, editPost } from '@/api/posts';
import bus from '@/utils/bus.js';
export default {
  data() {
    return {
      title: '',
      contents: '',
      logMessage: '',
    };
  },
  computed: {
    // 게시물 내용이 너무 많을 때 유효성 검사 - 200자 이하
    isContentsValid() {
      return this.contents.length <= 200;
    },
  },
  methods: {
    async submitForm() {
      const id = this.$route.params.id;
      try {
        const response = await editPost(id, {
          title: this.title,
          contents: this.contents,
        });
        bus.$emit(
          'show:toast',
          `${response.data.title} 게시글이 수정되었습니다.`,
        );
        this.$router.push('/main');
      } catch (error) {
        this.logMessage = error;
        alert('수정 실패');
      }
    },
  },
  // 수정 폼 컴포넌트 생성되자마자 호출 되야 함
  async created() {
    // 다이나믹 라우트 path에 명시한 id
    const id = this.$route.params.id;
    const { data } = await fetchOnePost(id);
    this.title = data.title;
    this.contents = data.contents;
  },
};
</script>

<style scoped>
.form-wrapper .form {
  width: 100%;
}
.btn {
  color: white;
}
</style>
