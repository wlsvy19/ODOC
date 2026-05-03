package com.example.board.post;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostResponse> findAll() {
        return postRepository.findAllByOrderByIdDesc().stream()
                .map(PostResponse::from)
                .toList();
    }

    public PostResponse findById(Long id) {
        return postRepository.findById(id)
                .map(PostResponse::from)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @Transactional
    public PostResponse create(PostRequest request) {
        Post post = new Post(request.title(), request.author(), request.content());
        return PostResponse.from(postRepository.save(post));
    }

    @Transactional
    public PostResponse update(Long id, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        post.update(request.title(), request.author(), request.content());
        return PostResponse.from(post);
    }

    @Transactional
    public void delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new PostNotFoundException(id);
        }
        postRepository.deleteById(id);
    }
}

