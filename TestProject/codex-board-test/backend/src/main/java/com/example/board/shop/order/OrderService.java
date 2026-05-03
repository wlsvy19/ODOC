package com.example.board.shop.order;

import com.example.board.shop.auth.UserPrincipal;
import com.example.board.shop.common.ApiException;
import com.example.board.shop.common.NotFoundException;
import com.example.board.shop.product.Product;
import com.example.board.shop.product.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<OrderResponse> findAll(UserPrincipal user) {
        List<ShopOrder> orders = user.admin()
                ? orderRepository.findAllByOrderByIdDesc()
                : orderRepository.findAllByUsernameOrderByIdDesc(user.username());
        return orders.stream()
                .map(OrderResponse::from)
                .toList();
    }

    public OrderResponse findById(UserPrincipal user, Long id) {
        ShopOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found: " + id));
        if (!user.admin() && !order.getUsername().equals(user.username())) {
            throw new ApiException("You can only view your own orders.");
        }
        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse create(UserPrincipal user, OrderRequest request) {
        ShopOrder order = new ShopOrder(user.username(), new CustomerInfo(
                request.customerName(),
                request.email(),
                request.phone(),
                request.address()
        ));

        for (OrderItemRequest itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new NotFoundException("Product not found: " + itemRequest.productId()));
            product.decreaseStock(itemRequest.quantity());
            order.addItem(new OrderItem(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    itemRequest.quantity()
            ));
        }

        return OrderResponse.from(orderRepository.save(order));
    }
}
