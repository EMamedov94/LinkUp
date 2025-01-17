package com.example.linkup.services.profile;

import com.example.linkup.models.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserProfileService {
    User findUserProfile(Long id);
}
