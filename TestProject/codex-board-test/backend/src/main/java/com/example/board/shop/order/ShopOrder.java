package com.example.board.shop.order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "shop_orders")
public class ShopOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private CustomerInfo customer;

    @Column(nullable = false, length = 40)
    private String username;

    @Column(nullable = false)
    private int totalAmount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime orderedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    protected ShopOrder() {
    }

    public ShopOrder(String username, CustomerInfo customer) {
        this.username = username;
        this.customer = customer;
    }

    @PrePersist
    void prePersist() {
        orderedAt = LocalDateTime.now();
    }

    public void addItem(OrderItem item) {
        item.setOrder(this);
        items.add(item);
        totalAmount += item.getLineTotal();
    }

    public Long getId() {
        return id;
    }

    public CustomerInfo getCustomer() {
        return customer;
    }

    public String getUsername() {
        return username;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}
