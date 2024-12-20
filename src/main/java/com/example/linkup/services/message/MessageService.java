package com.example.linkup.services.message;

import com.example.linkup.models.Message;
import com.example.linkup.models.User;
import com.example.linkup.models.dto.message.ChatRoomDto;

import java.util.List;

public interface MessageService {
    Message saveMessage(User sender, User receiver, String text);
    List<Message> getMessagesBetweenUsers(String senderUsername, String receiverUsername);
    List<Message> getMessagesForUser(String receiverUsername);
    List<ChatRoomDto> getChatRoomsBetweenUsersByUsername(String username);

    List<Message> getMessagesInChat(Long id);
}
