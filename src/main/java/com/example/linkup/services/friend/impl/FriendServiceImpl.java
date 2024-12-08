package com.example.linkup.services.friend.impl;

import com.example.linkup.enums.FriendStatus;
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

    private boolean isFriend(Long userId, Long friendId) {
        if (friendshipRepository.areFriendsById(userId, friendId)) {
            throw new FriendshipAlreadyExistsException();
        } else {
            return true;
        }
    }

    private boolean hasSentFriendRequest(Long userId, Long friendId) {
        if (friendshipRepository.existsBySenderIdAndReceiverIdAndStatus(userId, friendId, FriendStatus.PENDING)) {
            throw new FriendshipAlreadyExistsException();
        }
        return true;
    }

    // Show accepted friend list
    @Override
    public List<User> showFriendsList(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        return friendshipRepository.findAcceptedFriendsListByUsername(user.getUsername());
    }

    // Show friend request list
    @Override
    public List<User> showFriendsRequestList(UserDetails userDetails) {
        return friendshipRepository.findFriendsRequestListByUsername(userDetails.getUsername());
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

//        User userDb = userRepository.findByUsername(currentUser);
//        Friendship friendship = friendshipRepository.findBySenderId(friendId);
//
//        if (!isFriend(userDb.getId(), friendId)) {
//            friendship.setStatus(FriendStatus.ACCEPTED);
//        }
//        friendshipRepository.save(friendship);
    }
}
