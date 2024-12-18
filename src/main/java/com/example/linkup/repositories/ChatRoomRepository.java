package com.example.linkup.repositories;

import com.example.linkup.models.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findChatRoomBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
