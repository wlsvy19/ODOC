package com.example.board.shop.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 500) String description,
        @NotBlank @Size(max = 40) String category,
        @Min(0) int price,
        @Min(0) int stock,
        @NotBlank @Size(max = 1000) String imageUrl
) {
}

