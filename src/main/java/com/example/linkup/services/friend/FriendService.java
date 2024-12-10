package com.example.linkup.services.friend;

import com.example.linkup.enums.FriendStatus;
import com.example.linkup.models.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface FriendService {
    List<User> showFriendsList(UserDetails userDetails);
    List<User> showFriendsRequestList(UserDetails userDetails);
    void sendFriendRequest(Long userId, Long friendId);
    void acceptFriendRequest(String currentUser, Long friendId);
    void deleteFriend(UserDetails userDetails, Long friendId);
    boolean isFriend(Long userId, Long friendId);
    Optional<FriendStatus> getFriendStatus(Long userId, Long friendId);
}
