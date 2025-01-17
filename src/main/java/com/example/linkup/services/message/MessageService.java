package com.example.linkup.services.message;

import com.example.linkup.models.ChatRoom;
import com.example.linkup.models.Message;
import com.example.linkup.models.User;
import com.example.linkup.models.dto.message.MessageDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MessageService {
    Message saveMessage(MessageDto messageDto);
    List<ChatRoom> getChatRoomsBetweenUsersByUsername(String username);
    Page<Message> getMessagesInChat(Long id, int page, int size);
    Long getChatRoomIdByUserId(Long id);
    Long getOrCreateChatRoom(Long user1, Long user2);
    User getPartnerChatRoom(Long chatRoomId, String currentUser);
}
