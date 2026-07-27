package com.study.jpalab.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void findAllPosts() {
        List<Post> posts = postRepository.findAll();

        assertThat(posts).isNotEmpty();
        assertThat(posts).allSatisfy(post -> {
            assertThat(post.getId()).isNotNull();
            assertThat(post.getTitle()).isNotBlank();
            assertThat(post.getContent()).isNotBlank();
            assertThat(post.getAuthor()).isNotBlank();
            assertThat(post.getCreatedAt()).isNotNull();
            assertThat(post.getUpdatedAt()).isNotNull();
        });
    }
}