package com.study.jpalab.post.service.impl;

import com.study.jpalab.post.dto.PostForm;
import com.study.jpalab.post.entity.Post;
import com.study.jpalab.post.exception.PostNotFoundException;
import com.study.jpalab.post.repository.PostRepository;
import com.study.jpalab.post.service.PostService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll(
                Sort.by(Sort.Direction.DESC, "id")
        );
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @Override
    @Transactional
    public Post create(PostForm form) {
        Post post = Post.create(
                form.getTitle(),
                form.getContent(),
                form.getAuthor()
        );

        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post update(Long id, PostForm form) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        post.update(
                form.getTitle(),
                form.getContent(),
                form.getAuthor()
        );

        return post;
    }
}