<template>
  <div class="contents">
    <h1 class="page-header">Create Post</h1>
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
        <button type="submit" class="btn">Create</button>
      </form>
      <p class="log">
        {{ logMessage }}
      </p>
    </div>
  </div>
</template>

<script>
import { createPost } from '@/api/posts';
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
      try {
        // 게시물 Create 눌러서 생성
        const response = await createPost({
          title: this.title,
          contents: this.contents,
        });
        bus.$emit(
          'show:toast',
          `${response.data.data.title} 게시물이 등록되었습니다.`,
        );
        // 게시물 생성 후 main페이지로 이동
        this.$router.push('/main');
      } catch (error) {
        this.logMessage = error.response.data.message;
      }
    },
  },
};
</script>

<style scoped>
/* form-wrapper 안에 form이 있는 경우 */
.form-wrapper .form {
  width: 100%;
}
.btn {
  color: white;
}
</style>
