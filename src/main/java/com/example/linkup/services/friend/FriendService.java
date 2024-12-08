package com.example.linkup.services.friend;

import com.example.linkup.models.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface FriendService {
    List<User> showFriendsList(UserDetails userDetails);
    List<User> showFriendsRequestList(UserDetails userDetails);
    void addFriend(Long userId, Long friendId);
    void sendFriendRequest(Long userId, Long friendId);
    void acceptFriendRequest(String currentUser, Long friendId);
}
