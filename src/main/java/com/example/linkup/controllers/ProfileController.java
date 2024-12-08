package com.example.linkup.controllers;

import com.example.linkup.models.User;
import com.example.linkup.services.profile.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/profilePage")
    public String userProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userProfileService.showUserProfile(userDetails);
        model.addAttribute("user", user);
        return "/profile/profilePage";
    }

    @GetMapping("profilePage/{id}")
    public String userProfile(Model model, @PathVariable Long id) {
        User user = userProfileService.findUserProfile(id);
        model.addAttribute("user", user);
        return "/profile/profilePage";
    }
}
