package com.study.jpalab.post.dto;

import com.study.jpalab.post.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostForm {

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 200, message = "제목은 200자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotBlank(message = "작성자를 입력해주세요.")
    @Size(max = 50, message = "작성자는 50자 이하여야 합니다.")
    private String author;

    public PostForm() {
    }

    public static PostForm from(Post post) {
        PostForm form = new PostForm();
        form.title = post.getTitle();
        form.content = post.getContent();
        form.author = post.getAuthor();
        return form;
    }


}