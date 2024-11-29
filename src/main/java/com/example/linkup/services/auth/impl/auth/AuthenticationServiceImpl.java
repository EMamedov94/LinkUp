package com.example.linkup.services.auth.impl.auth;

import com.example.linkup.config.JwtService;
import com.example.linkup.enums.Role;
import com.example.linkup.models.User;
import com.example.linkup.repositories.UserRepository;
import com.example.linkup.services.auth.AuthenticationService;
import com.example.linkup.validations.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    // Login user
    @Override
    public User loginUser(User loginUser) {

        User user = validationService.validateCredentials(loginUser.getUsername(), loginUser.getPassword());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        var token = jwtService.generateToken(user);
        user.setToken(token);
        return user;
    }

    // Registration new user
    @Override
    public void registrationNewUser(User user) {
        validationService.validateNewUser(user);

        var newUser = User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(newUser);
    }
}
