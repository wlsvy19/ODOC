package com.study.jpalab.post;

import com.study.jpalab.post.entity.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostMappingTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void postEntityIsMapped() {
        var postEntity = entityManager.getMetamodel().entity(Post.class);

        assertThat(postEntity.getName()).isEqualTo("Post");
    }
}