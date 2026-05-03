package com.example.board.shop.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AnswerRequest(
        @NotBlank @Size(max = 1000) String answer
) {
}

