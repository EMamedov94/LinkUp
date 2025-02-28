package com.example.linkup.repositories;

import com.example.linkup.enums.FriendStatus;
import com.example.linkup.models.Friendship;
import com.example.linkup.models.User;
import com.example.linkup.models.projection.UserSearchProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Friendship f " +
            "SET f.status = 'ACCEPTED' " +
            "WHERE (f.sender.id IN (:user1, :user2) AND f.receiver.id IN (:user1, :user2)) ")
    void acceptFriendRequest(
            @Param("user1") Long user1,
            @Param("user2") Long user2
    );

    @Query("SELECT COUNT(f) > 0 " +
            "FROM Friendship f " +
            "WHERE (f.sender.id IN (:user1, :user2) AND f.receiver.id IN (:user1, :user2)) " +
            "AND f.status = 'ACCEPTED'")
    boolean areFriendsById(
            @Param("user1") Long user1,
            @Param("user2") Long user2
    );

    @Query("SELECT f.receiver.id as id, f.receiver.firstName as firstName, f.receiver.lastName as lastName " +
            "FROM Friendship f WHERE f.sender.username = :username AND f.status = 'ACCEPTED' " +
            "UNION " +
            "SELECT f.sender.id as id, f.sender.firstName as firstName, f.sender.lastName as lastName " +
            "FROM Friendship f WHERE f.receiver.username = :username AND f.status = 'ACCEPTED'" )
    List<UserSearchProjection> findAcceptedFriendsListByUsername(
            @Param("username") String username
    );

    @Query("SELECT f.sender " +
            "FROM Friendship f " +
            "WHERE f.status = 'PENDING' " +
            "AND f.receiver.username = :username")
    List<User> findFriendsRequestListByUsername(
            @Param("username") String username
    );

    @Query("SELECT f FROM Friendship f " +
            "WHERE (f.sender.id = :userId AND f.receiver.id = :friendId) " +
            "OR (f.sender.id = :friendId AND f.receiver.id = :userId) ")
    Optional<Friendship> findFriendshipByUserId(
            @Param("userId") Long userId,
            @Param("friendId") Long friendId
    );

    @Query("SELECT f FROM Friendship f " +
            "WHERE (f.sender.id = :userId AND f.receiver.id = :friendId) " +
            "OR (f.sender.id = :friendId AND f.receiver.id = :userId) ")
    Optional<Friendship> findFriendshipStatusByUserID(
            @Param("userId") Long userId,
            @Param("friendId") Long friendId
    );
}
