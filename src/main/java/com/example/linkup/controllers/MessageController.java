package com.example.linkup.controllers;

import com.example.linkup.models.Message;
import com.example.linkup.models.User;
import com.example.linkup.models.dto.message.MessageDto;
import com.example.linkup.services.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/messages/")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate brokerMessagingTemplate;

    // Get chat rooms
    @GetMapping("/")
    public String chatRooms(Principal currentUser, Model model) {
        model.addAttribute("chatRooms", messageService.getChatRoomsBetweenUsersByUsername(currentUser.getName()));
        return "profile/messages/chatRooms";
    }

    // Create or get chatRoom
    @PostMapping("/createChatRoom/{userId}")
    public String createChatRoom(@PathVariable Long userId, @AuthenticationPrincipal User user) {
        Long chatId = messageService.getOrCreateChatRoom(user.getId(), userId);
        return "redirect:/messages/" + chatId;
    }

    // Get messages list by chat id
    // Надо доделать, чтобы выводились послед сообщ
    @GetMapping("/{id}")
    public String messages(@PathVariable Long id,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           Principal user,
                           Model model) {
        Page<Message> messageList = messageService.getMessagesInChat(id, page, size);
        User chatPartner = messageService.getPartnerChatRoom(id, user.getName());


        model.addAttribute("chatId", id);
        model.addAttribute("messages", messageList);
        model.addAttribute("chatPartnerName", chatPartner);

        // Информация для пагинации
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", messageList.getTotalPages());
        model.addAttribute("totalMessages", messageList.getTotalElements());

        return "profile/messages/messagesPage";
    }

    // Send message
    @MessageMapping("/sendMessage/{chatId}")
    public void sendMessage(@DestinationVariable Long chatId,
                            @Payload MessageDto messageDto) {

        Message savedMessage = messageService.saveMessage(messageDto);
        messageDto.setTimestamp(savedMessage.getTimestamp());

        brokerMessagingTemplate.convertAndSendToUser(
                savedMessage.getReceiver().getUsername(),
                "/queue/messages/" + chatId,
                messageDto
        );
    }
}
