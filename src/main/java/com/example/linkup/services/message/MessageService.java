package com.example.linkup.services.message;

import com.example.linkup.models.ChatRoom;
import com.example.linkup.models.Message;
import com.example.linkup.models.User;

import java.util.List;

public interface MessageService {
    Message saveMessage(User sender, User receiver, String text);
    List<ChatRoom> getChatRoomsBetweenUsersByUsername(String username);
    String getPartnerNameByChatId(Long id);
    List<Message> getMessagesInChat(Long id);
}
