package com.example.linkup.controllers;

import com.example.linkup.enums.FriendStatus;
import com.example.linkup.models.User;
import com.example.linkup.services.friend.FriendService;
import com.example.linkup.services.message.MessageService;
import com.example.linkup.services.profile.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final FriendService friendService;
    private final MessageService messageService;

    // Get profile by user id
    @GetMapping("/profilePage/{id}")
    public String userProfile(@AuthenticationPrincipal User user,
                              Model model, @PathVariable Long id) {

        User userDb = userProfileService.findUserProfile(id);
        Optional<FriendStatus> friendStatus = friendService.getFriendStatus(user.getId(), userDb.getId());

        model.addAttribute("user", userDb);
        model.addAttribute("chatId", messageService.getChatRoomIdByUserId(id));
        model.addAttribute("friendStatus", friendStatus);
        model.addAttribute("areFriends", friendService.isFriend(user.getId(), userDb.getId()));
        model.addAttribute("isOwnProfile", user.getId().equals(id));

        return "/profile/profilePage";
    }
}
