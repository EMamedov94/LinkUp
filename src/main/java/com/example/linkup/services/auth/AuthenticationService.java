package com.example.linkup.services.auth;

import com.example.linkup.models.User;
import com.example.linkup.models.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {
    User loginUser(UserDto loginUser);
    void registrationNewUser(User userDto);
    void completeRegistrationNewUser(UserDetails userDetails, String firstName, String lastName);
}
