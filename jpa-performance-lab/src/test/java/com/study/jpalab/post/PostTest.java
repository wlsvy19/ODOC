package com.study.jpalab.post;

import com.study.jpalab.post.entity.Post;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    void createPost() {
        Post post = Post.create("제목", "내용", "작성자");

        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용");
        assertThat(post.getAuthor()).isEqualTo("작성자");
    }

    @Test
    void updatePost() {
        Post post = Post.create("이전 제목", "이전 내용", "이전 작성자");

        post.update("수정 제목", "수정 내용", "수정 작성자");

        assertThat(post.getTitle()).isEqualTo("수정 제목");
        assertThat(post.getContent()).isEqualTo("수정 내용");
        assertThat(post.getAuthor()).isEqualTo("수정 작성자");
    }
}