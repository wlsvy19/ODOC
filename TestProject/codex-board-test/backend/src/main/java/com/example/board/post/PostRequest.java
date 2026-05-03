package com.example.board.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @NotBlank @Size(max = 120) String title,
        @NotBlank @Size(max = 40) String author,
        @NotBlank String content
) {
}

