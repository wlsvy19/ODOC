// 게시물 CURD 와 관련된 API

import { posts } from './index';

// 전체 학습 노트 목록 조회하는 API
function fetchAllPost() {
  return posts.get('/');
}

// 특정 학습 노트 조회하는 API
function fetchOnePost(postId) {
  return posts.get(postId);
}

// 학습 노트 작성 하는 API
function createPost(postData) {
  return posts.post('/', postData);
}

// 학습 노트 삭제 API
function deletePost(postId) {
  return posts.delete(postId);
}

// 학습 노트 수정 API
/**
 *
 * @param {*} postId : 게시글 고유 ID
 * @param {*} postData : 수정할 때 넘길 데이터
 * @returns
 */
function editPost(postId, postData) {
  return posts.put(postId, postData);
}

export { fetchAllPost, fetchOnePost, createPost, deletePost, editPost };
