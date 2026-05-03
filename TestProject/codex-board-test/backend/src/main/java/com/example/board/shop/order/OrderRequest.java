package com.example.board.shop.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record OrderRequest(
        @NotBlank @Size(max = 40) String customerName,
        @NotBlank @Email @Size(max = 120) String email,
        @NotBlank @Size(max = 30) String phone,
        @NotBlank @Size(max = 300) String address,
        @NotEmpty List<@Valid OrderItemRequest> items
) {
}

