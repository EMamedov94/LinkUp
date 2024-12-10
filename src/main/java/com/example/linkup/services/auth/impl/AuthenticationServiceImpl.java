package com.example.linkup.services.auth.impl;

import com.example.linkup.enums.Role;
import com.example.linkup.models.User;
import com.example.linkup.models.dto.auth.CompleteRegistrationNewUserDto;
import com.example.linkup.models.dto.auth.LoginUserDto;
import com.example.linkup.models.dto.auth.RegistrationNewUserDto;
import com.example.linkup.repositories.UserRepository;
import com.example.linkup.services.auth.AuthenticationService;
import com.example.linkup.services.token.TokenService;
import com.example.linkup.validations.ValidationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;


    // Login user
    @Override
    public User loginUser(LoginUserDto userDto, HttpServletResponse response) {
//        validationService.validateCredentials(userDto.getUsername(), userDto.getPassword());
        User userDb = userRepository.findByUsername(userDto.getUsername());

        tokenService.addTokenToCookie(userDb, response);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.getUsername(),
                        userDto.getPassword()
                )
        );

        return userDb;
    }

    // Registration new user
    @Override
    public void registrationNewUser(RegistrationNewUserDto userDto, HttpServletResponse response) {
        validationService.validateNewUser(userDto);

        User newUser = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(newUser);

        tokenService.addTokenToCookie(newUser, response);
    }

    // Complete registration new user
    @Override
    public void completeRegistrationNewUser(UserDetails userDetails, CompleteRegistrationNewUserDto userDto) {
        User userDb = userRepository.findByUsername(userDetails.getUsername());
        userDb.setFirstName(userDto.getFirstName());
        userDb.setLastName(userDto.getLastName());
        userRepository.save(userDb);
    }
}
