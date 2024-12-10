package com.example.linkup.validations.impl;

import com.example.linkup.exceptions.InvalidLoginOrPasswordException;
import com.example.linkup.exceptions.UserAlreadyExistsException;
import com.example.linkup.models.User;
import com.example.linkup.models.dto.UserDto;
import com.example.linkup.repositories.UserRepository;
import com.example.linkup.validations.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Validation new user for registration
    @Override
    public void validateNewUser(User userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UserAlreadyExistsException();
        }
    }

    // Validation username and password for login
    @Override
    public User validateCredentials(String username, String password) {
        User userDb = userRepository.findByUsername(username);

        if (userDb == null) {
            throw new InvalidLoginOrPasswordException();
        }

        if (!passwordEncoder.matches(password, userDb.getPassword())) {
            throw new InvalidLoginOrPasswordException();
        }

        return userDb;
    }
}
