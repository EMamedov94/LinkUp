package com.example.linkup.services.profile.impl;

import com.example.linkup.models.User;
import com.example.linkup.repositories.UserRepository;
import com.example.linkup.services.profile.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserRepository userRepository;

    // Show user profile by id
    @Override
    public User findUserProfile(Long id) {
        return userRepository.findById(id).orElse(new User());
    }

    @Override
    public User showUserProfile(User user) {
        return userRepository.findByUsername(user.getUsername());
    }
}
