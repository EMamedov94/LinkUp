package com.example.linkup.services.message.impl;

import com.example.linkup.exceptions.ChatRoomNotFoundException;
import com.example.linkup.models.ChatRoom;
import com.example.linkup.models.Message;
import com.example.linkup.models.User;
import com.example.linkup.models.dto.message.MessageDto;
import com.example.linkup.repositories.ChatRoomRepository;
import com.example.linkup.repositories.MessageRepository;
import com.example.linkup.repositories.UserRepository;
import com.example.linkup.services.message.MessageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    // Save new message
    @Override
//    @CacheEvict(value = "messages", key = "#messageDto.chatRoomId")
    public Message saveMessage(MessageDto messageDto) {
        ChatRoom chatRoomDb = chatRoomRepository.findById(messageDto.getChatRoomId())
                .orElseThrow(ChatRoomNotFoundException::new);

        User sender = userRepository.findById(messageDto.getSenderId())
                .orElseThrow(() -> new EntityNotFoundException("Sender not found"));

        User receiver = userRepository.findById(messageDto.getReceiverId())
                .orElseThrow(() -> new EntityNotFoundException("Receiver not found"));

        return messageRepository.save(
                Message.builder()
                        .chatRoom(chatRoomDb)
                        .sender(sender)
                        .receiver(receiver)
                        .text(messageDto.getText())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // Get chat rooms with 2 users
    @Override
    public List<ChatRoom> getChatRoomsBetweenUsersByUsername(String username) {
        User currentUser = userRepository.findByUsername(username);
        return chatRoomRepository.findChatRoomsWithLastMessages(currentUser.getId());
    }

    @Override
//    @Cacheable("messages")
    public Page<Message> getMessagesInChat(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "timestamp"));
        return messageRepository.findAllByChatRoomId(id, pageable);
    }

    @Override
    public Long getChatRoomIdByUserId(Long id) {
        return chatRoomRepository.findChatRoomByUserId(id);
    }

    @Override
    public User getPartnerChatRoom(Long chatRoomId, String currentUser) {
        ChatRoom room = chatRoomRepository.findById(chatRoomId).orElseThrow(ChatRoomNotFoundException::new);

        if (room.getSender().getUsername().equals(currentUser)) {
            return room.getReceiver();
        } else if (room.getReceiver().getUsername().equals(currentUser)) {
            return room.getSender();
        } else {
            throw new IllegalArgumentException("Current user is not part of this chat");
        }
    }

    @Override
    public Long getOrCreateChatRoom(Long user1, Long user2) {
        Optional<ChatRoom> existingChat = chatRoomRepository.findChatRoomBySenderAndReceiver(user1, user2);
        User sender = userRepository.findById(user1)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepository.findById(user2)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        if (existingChat.isPresent()) {
            return existingChat.get().getId();
        }

        // Если чата нет, создаем новый
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setSender(sender);
        chatRoom.setReceiver(receiver);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return savedChatRoom.getId();
    }
}
