package com.example.linkup.services.friend;

import com.example.linkup.enums.FriendStatus;
import com.example.linkup.models.User;
import com.example.linkup.models.dto.user.UserSearchDto;
import com.example.linkup.models.projection.UserSearchProjection;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface FriendService {
    List<UserSearchProjection> showFriendsList(String username);
    List<User> showFriendsRequestList(String username);
    Page<UserSearchProjection> showAllActiveUsersFromDb();
    List<UserSearchProjection> findByFilters(String query);
    void sendFriendRequest(Long userId, Long friendId);
    void acceptFriendRequest(String currentUser, Long friendId);
    void deleteFriend(String username, Long friendId);
    boolean isFriend(Long userId, Long friendId);
    Optional<FriendStatus> getFriendStatus(Long userId, Long friendId);
}
