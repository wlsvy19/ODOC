package com.example.board.shop.order;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CustomerInfo {

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(nullable = false, length = 300)
    private String address;

    protected CustomerInfo() {
    }

    public CustomerInfo(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}

