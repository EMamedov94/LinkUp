package com.example.linkup.services.friend.impl;

import com.example.linkup.enums.FriendStatus;
import com.example.linkup.exceptions.FriendRequestAlreadySentException;
import com.example.linkup.exceptions.FriendshipAlreadyExistsException;
import com.example.linkup.exceptions.FriendshipNotFoundException;
import com.example.linkup.models.Friendship;
import com.example.linkup.models.User;
import com.example.linkup.repositories.FriendshipRepository;
import com.example.linkup.repositories.UserRepository;
import com.example.linkup.services.friend.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    // Show accepted friend list
    @Override
    public List<User> showFriendsList(UserDetails userDetails) {
        User userDb = userRepository.findByUsername(userDetails.getUsername());
        return friendshipRepository.findAcceptedFriendsListByUsername(userDb.getUsername());
    }

    // Show friend request list
    @Override
    public List<User> showFriendsRequestList(UserDetails userDetails) {
        return friendshipRepository.findFriendsRequestListByUsername(userDetails.getUsername());
    }

    // Send friend request
    @Override
    public void sendFriendRequest(Long userId, Long friendId) {
        Friendship friendshipBd = friendshipRepository.findFriendshipByUserId(userId, friendId)
                .orElse(new Friendship());

        if (friendshipBd.getStatus() == FriendStatus.REJECTED) {
            friendshipBd.setStatus(FriendStatus.PENDING);
        } else if (friendshipBd.getStatus() == FriendStatus.ACCEPTED) {
            friendshipBd.setStatus(FriendStatus.REJECTED);
        } else if (friendshipBd.getStatus() == FriendStatus.PENDING) {
            return;
        }

        friendshipBd.setSender(new User(userId));
        friendshipBd.setReceiver(new User(friendId));
        friendshipBd.setStatus(FriendStatus.PENDING);

        friendshipRepository.save(friendshipBd);
    }

    // Accept friend request
    @Override
    public void acceptFriendRequest(String currentUser, Long friendId) {
        User userDb = userRepository.findByUsername(currentUser);

        if (!isFriend(userDb.getId(), friendId)) {
            friendshipRepository.acceptFriendRequest(userDb.getId(), friendId);
        }
    }

    // Delete friend
    @Override
    public void deleteFriend(UserDetails userDetails, Long friendId) {
        User userDb = userRepository.findByUsername(userDetails.getUsername());

        Friendship friendship = friendshipRepository.findFriendshipByUserId(userDb.getId(), friendId)
                .orElseThrow(FriendshipNotFoundException::new);
        friendship.setStatus(FriendStatus.REJECTED);
        friendshipRepository.save(friendship);

    }

    // Check friends is already exists
    @Override
    public boolean isFriend(Long userId, Long friendId) {
        return friendshipRepository.areFriendsById(userId, friendId);
    }

    @Override
    public Optional<FriendStatus> getFriendStatus(Long userId, Long friendId) {
        return friendshipRepository.findFriendshipStatusByUserID(userId, friendId)
                .map(Friendship::getStatus);
    }
}
