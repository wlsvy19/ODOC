package com.example.board.shop.order;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String username,
        String customerName,
        String email,
        String phone,
        String address,
        int totalAmount,
        LocalDateTime orderedAt,
        List<OrderItemResponse> items
) {
    static OrderResponse from(ShopOrder order) {
        return new OrderResponse(
                order.getId(),
                order.getUsername(),
                order.getCustomer().getName(),
                order.getCustomer().getEmail(),
                order.getCustomer().getPhone(),
                order.getCustomer().getAddress(),
                order.getTotalAmount(),
                order.getOrderedAt(),
                order.getItems().stream().map(OrderItemResponse::from).toList()
        );
    }
}
