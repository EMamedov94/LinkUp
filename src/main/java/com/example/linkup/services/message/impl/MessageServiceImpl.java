package com.example.linkup.services.message.impl;

import com.example.linkup.models.ChatRoom;
import com.example.linkup.models.Message;
import com.example.linkup.models.User;
import com.example.linkup.models.dto.message.MessageDto;
import com.example.linkup.repositories.ChatRoomRepository;
import com.example.linkup.repositories.MessageRepository;
import com.example.linkup.repositories.UserRepository;
import com.example.linkup.services.message.MessageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    // Save new message
    @Override
    @Transactional
    public Message saveMessage(MessageDto messageDto) {
        ChatRoom chatRoomDb = chatRoomRepository.findById(messageDto.getChatRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Chat room not found"));

        User sender = userRepository.findById(messageDto.getSenderId())
                .orElseThrow(() -> new EntityNotFoundException("Sender not found"));

        User receiver = userRepository.findById(messageDto.getReceiverId())
                .orElseThrow(() -> new EntityNotFoundException("Receiver not found"));

        Message message = new Message();
        message.setChatRoom(chatRoomDb);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setText(messageDto.getText());
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    // Get chat rooms with 2 users
    @Override
    public List<ChatRoom> getChatRoomsBetweenUsersByUsername(String username) {
        User currentUser = userRepository.findByUsername(username);

        return chatRoomRepository.findChatRoomsWithLastMessages(currentUser.getId());
    }

    @Override
    public List<Message> getMessagesInChat(Long id) {
        return messageRepository.findAllByChatRoomIdOrderByTimestampAsc(id);
    }
}
