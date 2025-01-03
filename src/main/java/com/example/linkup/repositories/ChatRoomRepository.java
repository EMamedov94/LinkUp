package com.example.linkup.repositories;

import com.example.linkup.models.ChatRoom;
import com.example.linkup.models.dto.message.ChatRoomDto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT c FROM ChatRoom c " +
            "JOIN FETCH c.messages m " +
            "WHERE (c.sender.id = :userId OR c.receiver.id = :userId) " +
            "AND m.timestamp = (SELECT MAX(msg.timestamp) FROM Message msg WHERE msg.chatRoom.id = c.id) " +
            "ORDER BY m.timestamp DESC")
    List<ChatRoom> findChatRoomsWithLastMessages(@Param("userId") Long userId);

    @Query("SELECT c.id FROM ChatRoom c " +
            "WHERE c.sender.id = :userId OR c.receiver.id = :userId ")
    Long findChatRoomByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM ChatRoom c " +
            "WHERE (c.sender.id = :user1 AND c.receiver.id = :user2) " +
            "OR (c.sender.id = :user2 AND c.receiver.id = :user1)")
    Optional<ChatRoom> findChatRoomBySenderAndReceiver(@Param("user1") Long user1, @Param("user2") Long user2);
}
