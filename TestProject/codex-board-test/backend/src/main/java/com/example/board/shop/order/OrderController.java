package com.example.board.shop.order;

import com.example.board.shop.auth.AuthService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final AuthService authService;

    public OrderController(OrderService orderService, AuthService authService) {
        this.orderService = orderService;
        this.authService = authService;
    }

    @GetMapping
    public List<OrderResponse> findAll(@RequestHeader("X-User-Id") String username) {
        return orderService.findAll(authService.requireUser(username));
    }

    @GetMapping("/{id}")
    public OrderResponse findById(@RequestHeader("X-User-Id") String username, @PathVariable Long id) {
        return orderService.findById(authService.requireUser(username), id);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(
            @RequestHeader("X-User-Id") String username,
            @Valid @RequestBody OrderRequest request
    ) {
        OrderResponse response = orderService.create(authService.requireUser(username), request);
        return ResponseEntity.created(URI.create("/api/orders/" + response.id())).body(response);
    }
}
