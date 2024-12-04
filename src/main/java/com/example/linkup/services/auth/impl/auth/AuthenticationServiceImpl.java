package com.example.linkup.services.auth.impl.auth;

import com.example.linkup.config.JwtService;
import com.example.linkup.enums.Role;
import com.example.linkup.models.User;
import com.example.linkup.models.dto.UserDto;
import com.example.linkup.repositories.UserRepository;
import com.example.linkup.services.auth.AuthenticationService;
import com.example.linkup.validations.ValidationService;
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
    private final JwtService jwtService;


    // Login user
    @Override
    public User loginUser(UserDto loginUser) {
        User user = validationService.validateCredentials(loginUser.getUsername(), loginUser.getPassword());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        var token = jwtService.generateToken(user);
        user.setToken(token);
        return user;
    }

    // Registration new user
    @Override
    public void registrationNewUser(User userDto) {
        validationService.validateNewUser(userDto);

        User newUser = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(newUser);
    }

    // Complete registration new user
    @Override
    public void completeRegistrationNewUser(UserDetails userDetails, String firstName, String lastName) {
        User userDb = userRepository.findByUsername(userDetails.getUsername());
        userDb.setFirstName(firstName);
        userDb.setLastName(lastName);
        userRepository.save(userDb);
    }
}
