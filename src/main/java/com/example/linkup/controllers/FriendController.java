package com.example.linkup.controllers;

import com.example.linkup.models.User;
import com.example.linkup.services.friend.FriendService;
import com.example.linkup.services.profile.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;
    private final UserProfileService userProfileService;

    // Friends list
    @GetMapping("/friendsList")
    public String fiendsListPage(Model model,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        List<User> friendsList = friendService.showFriendsList(userDetails);
        model.addAttribute("friends", friendsList);

        return "/profile/friends/friendsPage";
    }

    // Friends requests
    @GetMapping("/friendsRequests")
    public String friendsRequestsPage(Model model,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        List<User> requestsList = friendService.showFriendsRequestList(userDetails);
        model.addAttribute("friends", requestsList);

        return "/profile/friends/friendsRequestsPage";
    }

    // Send friend request
    @PostMapping("/sendFriendRequest/{id}")
    public String sendFriendRequest(@PathVariable Long id,
                            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userProfileService.showUserProfile(userDetails);
        friendService.sendFriendRequest(currentUser.getId(), id);

        return "redirect:/profile/profilePage/" + id;
    }

    // Accept friend request
    @PostMapping("/acceptFriend/{id}")
    public String acceptFriend(@PathVariable Long id,
                               @AuthenticationPrincipal UserDetails userDetails) {
        friendService.acceptFriendRequest(userDetails.getUsername(), id);

        return "/profile/friends/friendsRequestsPage";
    }

    @PostMapping("/deleteFriend/{id}")
    public String deleteUser(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails userDetails) {
        friendService.deleteFriend(userDetails, id);

        return "redirect:/profile/profilePage/" + id;
    }
}
