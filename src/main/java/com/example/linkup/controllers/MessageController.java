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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/messages/")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate brokerMessagingTemplate;

    @GetMapping("/")
    public String chatRooms(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("chatRooms", messageService.getChatRoomsBetweenUsersByUsername(userDetails.getUsername()));
        return "profile/messages/chatRooms";
    }

    // Get messages list by chat id
    // Надо доделать, чтобы выводились послед сообщ
    @GetMapping("/{id}")
    public String messages(@PathVariable Long id,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @AuthenticationPrincipal UserDetails userDetails,
                           Model model) {
        Page<Message> messageList = messageService.getMessagesInChat(id, page, size);

        User chatPartner = messageList.stream().findFirst()
                .map(message -> {
                    User sender = message.getSender();
                    User receiver = message.getReceiver();
                    if (!sender.getUsername().equals(userDetails.getUsername())) {
                        return sender;
                    }
                    return receiver;
                }).orElse(new User());

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
