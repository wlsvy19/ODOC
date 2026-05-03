package com.example.board.shop.auth;

public record LoginResponse(
        String username,
        boolean admin
) {
    static LoginResponse from(UserPrincipal user) {
        return new LoginResponse(user.username(), user.admin());
    }
}

