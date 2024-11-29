package com.example.linkup.services.auth;

import com.example.linkup.models.User;

public interface AuthenticationService {
    User loginUser(User loginUser);
    void registrationNewUser(User user);
}
