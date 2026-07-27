package com.study.jpalab.post.controller;

import com.study.jpalab.post.dto.PostForm;
import com.study.jpalab.post.service.PostService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("posts", postService.findAll());
        return "posts/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.findById(id));
        return "posts/detail";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("postForm", new PostForm());
        model.addAttribute("pageTitle", "게시글 작성");
        model.addAttribute("formAction", "/posts");
        model.addAttribute("submitLabel", "저장");

        return "posts/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("postForm") PostForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "게시글 작성");
            model.addAttribute("formAction", "/posts");
            model.addAttribute("submitLabel", "저장");

            return "posts/form";
        }

        Long postId = postService.create(form).getId();

        return "redirect:/posts/" + postId;
    }
}