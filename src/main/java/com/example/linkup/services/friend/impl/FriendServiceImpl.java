package com.example.linkup.services.friend.impl;

import com.example.linkup.enums.FriendStatus;
import com.example.linkup.exceptions.FriendRequestAlreadySentException;
import com.example.linkup.exceptions.FriendshipAlreadyExistsException;
import com.example.linkup.models.Friendship;
import com.example.linkup.models.User;
import com.example.linkup.repositories.FriendshipRepository;
import com.example.linkup.repositories.UserRepository;
import com.example.linkup.services.friend.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    // Show accepted friend list
    @Override
    public List<User> showFriendsList(User user) {
        User userDb = userRepository.findByUsername(user.getUsername());
        return friendshipRepository.findAcceptedFriendsListByUsername(userDb.getUsername());
    }

    // Show friend request list
    @Override
    public List<User> showFriendsRequestList(User user) {
        return friendshipRepository.findFriendsRequestListByUsername(user.getUsername());
    }

    // Send friend request
    @Override
    public void sendFriendRequest(Long userId, Long friendId) {
        if (hasSentFriendRequest(userId, friendId)) {
            Friendship friendship = Friendship.builder()
                    .sender(new User(userId))
                    .receiver(new User(friendId))
                    .status(FriendStatus.PENDING)
                    .build();
            friendshipRepository.save(friendship);
        }
    }

    // Accept friend request
    @Override
    public void acceptFriendRequest(String currentUser, Long friendId) {
        User userDb = userRepository.findByUsername(currentUser);

        if (isFriend(userDb.getId(), friendId)) {
            friendshipRepository.acceptFriendRequest(userDb.getId(), friendId);
        }
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {

    }

    // Check friends is already exists
    @Override
    public boolean isFriend(Long userId, Long friendId) {
        return !friendshipRepository.areFriendsById(userId, friendId);
    }

    // Check requests on DB
    private boolean hasSentFriendRequest(Long userId, Long friendId) {
        if (friendshipRepository.existsBySenderIdAndReceiverIdAndStatus(userId, friendId, FriendStatus.PENDING)) {
            throw new FriendRequestAlreadySentException();
        }
        return true;
    }
}
