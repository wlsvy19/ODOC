package com.example.board.shop.auth;

public record UserPrincipal(
        String username,
        String password,
        boolean admin
) {
    public UserPrincipal withoutPassword() {
        return new UserPrincipal(username, "", admin);
    }
}

