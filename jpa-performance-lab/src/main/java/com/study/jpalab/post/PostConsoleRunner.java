package com.study.jpalab.post;

import com.study.jpalab.post.entity.Post;
import com.study.jpalab.post.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
public class PostConsoleRunner implements CommandLineRunner {

    private final PostRepository postRepository;

    public PostConsoleRunner(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) {
        List<Post> posts = postRepository.findAll();

        System.out.println("=== 게시글 전체 조회 결과 ===");

        if (posts.isEmpty()) {
            System.out.println("조회된 게시글이 없습니다.");
            return;
        }

        posts.forEach(post -> System.out.printf(
                "id=%d, title=%s, content=%s, author=%s, createdAt=%s, updatedAt=%s%n",
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        ));
    }
}
