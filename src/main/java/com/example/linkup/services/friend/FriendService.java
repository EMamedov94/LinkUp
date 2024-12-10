package com.example.linkup.services.friend;

import com.example.linkup.models.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface FriendService {
    List<User> showFriendsList(User user);
    List<User> showFriendsRequestList(User user);
    void sendFriendRequest(Long userId, Long friendId);
    void acceptFriendRequest(String currentUser, Long friendId);
    void deleteFriend(Long userId, Long friendId);
    boolean isFriend(Long userId, Long friendId);
}
