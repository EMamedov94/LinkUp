package com.example.linkup.services.message;

import com.example.linkup.models.ChatRoom;
import com.example.linkup.models.Message;
import com.example.linkup.models.User;

import java.util.List;

public interface MessageService {
    Message saveMessage(User sender, User receiver, String text);
    List<Message> getMessagesBetweenUsers(String senderUsername, String receiverUsername);
    List<Message> getMessagesForUser(String receiverUsername);
    List<ChatRoom> getChatRoomsBetweenUsers(String senderUsername, String receiverUsername);
}
