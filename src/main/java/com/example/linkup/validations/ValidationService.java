package com.example.linkup.validations;

import com.example.linkup.models.User;
import com.example.linkup.models.dto.UserDto;

public interface ValidationService {
    void validateNewUser(User userDto);
    User validateCredentials(String username, String password);
}
