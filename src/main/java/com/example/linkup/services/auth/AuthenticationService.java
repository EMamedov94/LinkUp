package com.example.linkup.services.auth;

import com.example.linkup.models.User;
import com.example.linkup.models.dto.auth.CompleteRegistrationNewUserDto;
import com.example.linkup.models.dto.auth.LoginUserDto;
import com.example.linkup.models.dto.auth.RegistrationNewUserDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {
    User loginUser(LoginUserDto userDto, HttpServletResponse response);
    void registrationNewUser(RegistrationNewUserDto userDto, HttpServletResponse response);
    void completeRegistrationNewUser(UserDetails userDetails, CompleteRegistrationNewUserDto userDto);
}
