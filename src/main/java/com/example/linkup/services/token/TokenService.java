package com.example.linkup.services.token;

import com.example.linkup.models.User;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenService {
    void addTokenToCookie(User user, HttpServletResponse response);
}
