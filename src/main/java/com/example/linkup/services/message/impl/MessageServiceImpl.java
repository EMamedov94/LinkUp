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

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    // Save new message
    @Override
    @CacheEvict(value = "messages", key = "#messageDto.chatRoomId")
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
    @Cacheable("messages")
    public Page<Message> getMessagesInChat(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "timestamp"));
        return messageRepository.findAllByChatRoomId(id, pageable);
    }

    @Override
    public Long getChatRoomIdByUserId(Long id) {
        return chatRoomRepository.findChatRoomByUserId(id);
    }

    private ChatRoom createNewChatRoom(Long userId) {
        // Создаем новый чат между двумя пользователями
        ChatRoom chatRoom = new ChatRoom();

        // Нужно задать sender и receiver в зависимости от логики
        User sender = ;
        User receiver = userProfileService.findUserProfile(userId); // получаем пользователя по id

        chatRoom.setSender(sender);
        chatRoom.setReceiver(receiver);

        // Сохраняем новый чат в базу
        return chatRoomRepository.save(chatRoom);
    }
}
