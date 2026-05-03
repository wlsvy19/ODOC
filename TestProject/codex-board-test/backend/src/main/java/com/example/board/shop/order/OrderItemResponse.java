package com.example.board.shop.order;

public record OrderItemResponse(
        Long productId,
        String productName,
        int unitPrice,
        int quantity,
        int lineTotal
) {
    static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.getProductId(),
                item.getProductName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getLineTotal()
        );
    }
}

