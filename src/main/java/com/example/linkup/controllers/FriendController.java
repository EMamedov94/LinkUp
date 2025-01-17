package com.example.linkup.controllers;

import com.example.linkup.models.User;
import com.example.linkup.models.dto.user.UserSearchDto;
import com.example.linkup.models.projection.UserSearchProjection;
import com.example.linkup.services.friend.FriendService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    // Global search friends
    @GetMapping("/globalSearch")
    public String globalSearch(Model model) {
        model.addAttribute("users", friendService.showAllActiveUsersFromDb());
        return "pages/globalSearch";
    }

    // Search friends with filters
    @GetMapping("/search")
    @ResponseBody
    public List<UserSearchProjection> searchUsers(@RequestParam("query") String query) {
        return friendService.findByFilters(query);
    }

    // Friends list
    @GetMapping("/friendsList")
    public String fiendsListPage(Model model,
                                 Principal currentUser) {
        model.addAttribute("friends", friendService.showFriendsList(currentUser.getName()));

        return "/profile/friends/friendsPage";
    }

    // Friends requests
    @GetMapping("/friendsRequests")
    public String friendsRequestsPage(Model model,
                                      Principal currentUser) {
        model.addAttribute("friends", friendService.showFriendsRequestList(currentUser.getName()));

        return "/profile/friends/friendsRequestsPage";
    }

    // Send friend request
    @PostMapping("/sendFriendRequest/{id}")
    public String sendFriendRequest(@PathVariable Long id,
                                    @AuthenticationPrincipal User currentUser) {
        friendService.sendFriendRequest(currentUser.getId(), id);

        return "redirect:/profile/profilePage/" + id;
    }

    // Accept friend request
    @PostMapping("/acceptFriend/{id}")
    public String acceptFriend(@PathVariable Long id,
                               Principal currentUser) {
        friendService.acceptFriendRequest(currentUser.getName(), id);

        return "/profile/friends/friendsRequestsPage";
    }

    @PostMapping("/deleteFriend/{id}")
    public String deleteUser(@PathVariable Long id,
                             Principal currentUser) {
        friendService.deleteFriend(currentUser.getName(), id);

        return "redirect:/profile/profilePage/" + id;
    }
}
