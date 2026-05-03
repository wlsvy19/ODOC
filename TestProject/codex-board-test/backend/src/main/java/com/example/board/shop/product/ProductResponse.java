package com.example.board.shop.product;

import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String name,
        String description,
        String category,
        int price,
        int stock,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}

