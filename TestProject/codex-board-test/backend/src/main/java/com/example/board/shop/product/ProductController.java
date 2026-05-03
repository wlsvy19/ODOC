package com.example.board.shop.product;

import com.example.board.shop.auth.AuthService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final AuthService authService;

    public ProductController(ProductService productService, AuthService authService) {
        this.productService = productService;
        this.authService = authService;
    }

    @GetMapping
    public List<ProductResponse> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @RequestHeader("X-User-Id") String username,
            @Valid @RequestBody ProductRequest request
    ) {
        authService.requireAdmin(username);
        ProductResponse response = productService.create(request);
        return ResponseEntity.created(URI.create("/api/products/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public ProductResponse update(
            @RequestHeader("X-User-Id") String username,
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request
    ) {
        authService.requireAdmin(username);
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("X-User-Id") String username,
            @PathVariable Long id
    ) {
        authService.requireAdmin(username);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
