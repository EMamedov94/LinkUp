package com.example.linkup.services.message;

import com.example.linkup.models.ChatRoom;
import com.example.linkup.models.Message;
import com.example.linkup.models.User;
import com.example.linkup.models.dto.message.MessageDto;

import java.util.List;

public interface MessageService {
    Message saveMessage(MessageDto messageDto);
    List<ChatRoom> getChatRoomsBetweenUsersByUsername(String username);
    List<Message> getMessagesInChat(Long id);
}
