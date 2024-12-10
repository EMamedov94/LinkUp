package com.example.linkup.validations;

import com.example.linkup.models.User;
import com.example.linkup.models.dto.auth.RegistrationNewUserDto;

public interface ValidationService {
    void validateNewUser(RegistrationNewUserDto userDto);
    User validateCredentials(String username, String password);
}
