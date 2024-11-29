package com.example.linkup.validations;

import com.example.linkup.models.User;

public interface ValidationService {
    void validateNewUser(User user);
    User validateCredentials(String username, String password);
}
