package com.example.linkup.repositories;

import com.example.linkup.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderUsernameAndReceiverUsername(String senderUsername, String receiverUsername);
    List<Message> findByReceiverUsername(String receiverUsername);
    List<Message> findAllByChatRoomIdOrderByTimestampAsc(Long chatRoomId);
}
