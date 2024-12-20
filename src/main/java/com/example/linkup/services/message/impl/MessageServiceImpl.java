package com.example.linkup.services.message.impl;

import com.example.linkup.models.Message;
import com.example.linkup.models.User;
import com.example.linkup.models.dto.message.ChatRoomDto;
import com.example.linkup.repositories.ChatRoomRepository;
import com.example.linkup.repositories.MessageRepository;
import com.example.linkup.repositories.UserRepository;
import com.example.linkup.services.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Override
    public Message saveMessage(User sender, User receiver, String text) {
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setText(text);
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesBetweenUsers(String senderUsername, String receiverUsername) {
        return messageRepository.findBySenderUsernameAndReceiverUsername(senderUsername, receiverUsername);
    }

    @Override
    public List<Message> getMessagesForUser(String receiverUsername) {
        return messageRepository.findByReceiverUsername(receiverUsername);
    }

    @Override
    public List<ChatRoomDto> getChatRoomsBetweenUsersByUsername(String username) {
        User currentUser = userRepository.findByUsername(username);

        return chatRoomRepository.findChatRoomsWithLastMessages(currentUser.getId());
    }

    @Override
    public List<Message> getMessagesInChat(Long id) {
        return messageRepository.findAllByChatRoomIdOrderByTimestampAsc(id);
    }
}
