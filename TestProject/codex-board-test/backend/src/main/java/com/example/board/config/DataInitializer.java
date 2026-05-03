package com.example.board.config;

import com.example.board.post.Post;
import com.example.board.post.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initPosts(PostRepository postRepository) {
        return args -> {
            if (postRepository.count() > 0) {
                return;
            }

            postRepository.save(new Post("첫 번째 게시글", "관리자", "Spring Boot와 Vue 3로 만든 게시판입니다."));
            postRepository.save(new Post("사용 안내", "관리자", "글을 작성하고, 선택한 뒤 수정하거나 삭제할 수 있습니다."));
        };
    }
}

