package com.example.board.shop.auth;

import com.example.board.shop.common.ApiException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public UserPrincipal login(String username, String password) {
        UserPrincipal user = findUser(username);
        if (!user.password().equals(password)) {
            throw new ApiException("Invalid username or password.");
        }
        return user.withoutPassword();
    }

    public UserPrincipal requireUser(String username) {
        return findUser(username).withoutPassword();
    }

    public UserPrincipal requireAdmin(String username) {
        UserPrincipal user = requireUser(username);
        if (!user.admin()) {
            throw new ApiException("Admin permission is required.");
        }
        return user;
    }

    private UserPrincipal findUser(String username) {
        if ("admin".equals(username)) {
            return new UserPrincipal("admin", "admin", true);
        }

        for (int i = 1; i <= 10; i++) {
            String user = "user" + i;
            if (user.equals(username)) {
                return new UserPrincipal(user, user, false);
            }
        }

        throw new ApiException("Invalid username or password.");
    }
}

