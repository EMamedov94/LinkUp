package com.example.linkup.validations;

import com.example.linkup.models.User;
import com.example.linkup.models.dto.UserDto;

public interface ValidationService {
    void validateNewUser(UserDto userDto);
    User validateCredentials(String username, String password);
}
