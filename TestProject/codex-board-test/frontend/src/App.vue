<script setup>
import { computed, onMounted, reactive, ref } from 'vue';
import { createPost, deletePost, getPost, getPosts, updatePost } from './api/posts';

const posts = ref([]);
const selectedPost = ref(null);
const loading = ref(false);
const saving = ref(false);
const error = ref('');
const form = reactive({
  id: null,
  title: '',
  author: '',
  content: '',
});

const isEditing = computed(() => form.id !== null);
const canSave = computed(() => form.title.trim() && form.author.trim() && form.content.trim());

function resetForm() {
  form.id = null;
  form.title = '';
  form.author = '';
  form.content = '';
}

function fillForm(post) {
  form.id = post.id;
  form.title = post.title;
  form.author = post.author;
  form.content = post.content;
}

function formatDate(value) {
  if (!value) {
    return '';
  }

  return new Intl.DateTimeFormat('ko-KR', {
    dateStyle: 'medium',
    timeStyle: 'short',
  }).format(new Date(value));
}

async function loadPosts() {
  loading.value = true;
  error.value = '';

  try {
    posts.value = await getPosts();
    if (!selectedPost.value && posts.value.length > 0) {
      await selectPost(posts.value[0].id);
    }
  } catch (err) {
    error.value = '게시글을 불러오지 못했습니다. 백엔드 서버가 실행 중인지 확인해주세요.';
  } finally {
    loading.value = false;
  }
}

async function selectPost(id) {
  error.value = '';

  try {
    selectedPost.value = await getPost(id);
  } catch (err) {
    error.value = '게시글 상세 정보를 불러오지 못했습니다.';
  }
}

async function submitForm() {
  if (!canSave.value) {
    return;
  }

  saving.value = true;
  error.value = '';

  const payload = {
    title: form.title.trim(),
    author: form.author.trim(),
    content: form.content.trim(),
  };

  try {
    const saved = isEditing.value ? await updatePost(form.id, payload) : await createPost(payload);
    selectedPost.value = saved;
    resetForm();
    await loadPosts();
  } catch (err) {
    error.value = '저장하지 못했습니다. 입력값을 확인해주세요.';
  } finally {
    saving.value = false;
  }
}

async function removePost(post) {
  const ok = window.confirm(`"${post.title}" 게시글을 삭제할까요?`);
  if (!ok) {
    return;
  }

  error.value = '';

  try {
    await deletePost(post.id);
    if (selectedPost.value?.id === post.id) {
      selectedPost.value = null;
    }
    if (form.id === post.id) {
      resetForm();
    }
    await loadPosts();
  } catch (err) {
    error.value = '게시글을 삭제하지 못했습니다.';
  }
}

onMounted(loadPosts);
</script>

<template>
  <main class="app-shell">
    <header class="topbar">
      <div>
        <p class="eyebrow">Spring Boot + Vue 3</p>
        <h1>게시판</h1>
      </div>
      <button class="secondary-button" type="button" @click="resetForm">새 글</button>
    </header>

    <p v-if="error" class="alert">{{ error }}</p>

    <section class="workspace">
      <aside class="post-list" aria-label="게시글 목록">
        <div class="panel-header">
          <h2>목록</h2>
          <span>{{ posts.length }}개</span>
        </div>

        <p v-if="loading" class="empty">불러오는 중...</p>
        <p v-else-if="posts.length === 0" class="empty">아직 게시글이 없습니다.</p>

        <button
          v-for="post in posts"
          :key="post.id"
          class="post-item"
          :class="{ active: selectedPost?.id === post.id }"
          type="button"
          @click="selectPost(post.id)"
        >
          <strong>{{ post.title }}</strong>
          <span>{{ post.author }} · {{ formatDate(post.createdAt) }}</span>
        </button>
      </aside>

      <section class="detail-panel" aria-label="게시글 상세">
        <article v-if="selectedPost" class="post-detail">
          <div class="detail-actions">
            <div>
              <h2>{{ selectedPost.title }}</h2>
              <p>{{ selectedPost.author }} · {{ formatDate(selectedPost.updatedAt) }}</p>
            </div>
            <div class="button-row">
              <button class="secondary-button" type="button" @click="fillForm(selectedPost)">수정</button>
              <button class="danger-button" type="button" @click="removePost(selectedPost)">삭제</button>
            </div>
          </div>
          <p class="content">{{ selectedPost.content }}</p>
        </article>
        <p v-else class="empty detail-empty">게시글을 선택해주세요.</p>
      </section>

      <form class="editor" @submit.prevent="submitForm">
        <div class="panel-header">
          <h2>{{ isEditing ? '글 수정' : '글 작성' }}</h2>
        </div>

        <label>
          제목
          <input v-model="form.title" maxlength="120" placeholder="제목을 입력하세요" />
        </label>

        <label>
          작성자
          <input v-model="form.author" maxlength="40" placeholder="작성자 이름" />
        </label>

        <label>
          내용
          <textarea v-model="form.content" rows="10" placeholder="내용을 입력하세요"></textarea>
        </label>

        <div class="button-row editor-actions">
          <button class="secondary-button" type="button" @click="resetForm">취소</button>
          <button class="primary-button" type="submit" :disabled="!canSave || saving">
            {{ saving ? '저장 중...' : '저장' }}
          </button>
        </div>
      </form>
    </section>
  </main>
</template>

