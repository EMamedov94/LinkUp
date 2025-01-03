package com.example.linkup.repositories;

import com.example.linkup.models.ChatRoom;
import com.example.linkup.models.dto.message.ChatRoomDto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
}
