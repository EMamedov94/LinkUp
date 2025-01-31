package com.example.linkup.services.friend.impl;

import com.example.linkup.models.User;
import com.example.linkup.models.projection.UserSearchProjection;
import com.example.linkup.repositories.FriendshipRepository;
import com.example.linkup.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FriendServiceImplTest {
    Long user1Id = 1L;
    Long user2Id = 2L;

    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendServiceImpl friendService;

    @Test
    void isFriend_WhenUsersIsFriend_ShouldReturnTrue() {
        Mockito.when(friendshipRepository.areFriendsById(user1Id, user2Id)).thenReturn(true);

        boolean result = friendService.isFriend(user1Id, user2Id);

        assertTrue(result);
        verify(friendshipRepository).areFriendsById(user1Id, user2Id);
    }

    @Test
    void isFriend_WhenUsersIsNotFriend_ShouldReturnFalse() {
        Mockito.when(friendshipRepository.areFriendsById(user1Id, user2Id)).thenReturn(false);

        boolean result = friendService.isFriend(user1Id, user2Id);

        assertFalse(result);
        verify(friendshipRepository).areFriendsById(user1Id, user2Id);
    }

    @Test
    void showFriendsList() {
        String username = "test";
        User mockUser = new User();
        mockUser.setUsername(username);

        UserSearchProjection friend1 = mock(UserSearchProjection.class);
        UserSearchProjection friend2 = mock(UserSearchProjection.class);
        List<UserSearchProjection> mockFriends = List.of(friend1, friend2);

        when(userRepository.findByUsername(username)).thenReturn(mockUser);
        when(friendshipRepository.findAcceptedFriendsListByUsername(username)).thenReturn(mockFriends);

        List<UserSearchProjection> friendsList = friendService.showFriendsList(username);

        assertNotNull(friendsList);
        assertEquals(2, friendsList.size());
        verify(userRepository).findByUsername(username);
        verify(friendshipRepository).findAcceptedFriendsListByUsername(username);
    }
}