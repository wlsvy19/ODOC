package com.study.jpalab.post.service;

import com.study.jpalab.post.dto.PostForm;
import com.study.jpalab.post.entity.Post;

import java.util.List;

public interface PostService {

    List<Post> findAll();

    Post findById(Long id);

    Post create(PostForm form);

    Post update(Long id, PostForm form);
}