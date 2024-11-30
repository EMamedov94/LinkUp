package com.example.linkup.services.auth;

import com.example.linkup.models.User;
import com.example.linkup.models.dto.UserDto;

public interface AuthenticationService {
    User loginUser(User loginUser);
    void registrationNewUser(UserDto userDto);
}
