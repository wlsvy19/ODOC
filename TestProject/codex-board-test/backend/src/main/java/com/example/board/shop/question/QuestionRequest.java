package com.example.board.shop.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record QuestionRequest(
        @NotBlank @Size(max = 120) String title,
        @NotBlank @Size(max = 1000) String content
) {
}

