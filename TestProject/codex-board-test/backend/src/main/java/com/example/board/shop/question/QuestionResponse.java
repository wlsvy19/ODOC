package com.example.board.shop.question;

import java.time.LocalDateTime;

public record QuestionResponse(
        Long id,
        String author,
        String title,
        String content,
        String answer,
        String answeredBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    static QuestionResponse from(Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getAuthor(),
                question.getTitle(),
                question.getContent(),
                question.getAnswer(),
                question.getAnsweredBy(),
                question.getCreatedAt(),
                question.getUpdatedAt()
        );
    }
}

