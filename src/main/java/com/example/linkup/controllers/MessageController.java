package com.example.linkup.controllers;

import com.example.linkup.models.ChatRoom;
import com.example.linkup.models.Message;
import com.example.linkup.models.User;
import com.example.linkup.models.dto.message.ChatRoomDto;
import com.example.linkup.models.dto.message.MessageDto;
import com.example.linkup.services.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/messages/")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate brokerMessagingTemplate;

    @GetMapping("/")
    public String chatRooms(Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {

        List<ChatRoom> chatRooms = messageService.getChatRoomsBetweenUsersByUsername(userDetails.getUsername());

        model.addAttribute("chatRooms", chatRooms);

        return "profile/messages/chatRooms";
    }

    @GetMapping("/{id}")
    public String messages(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetails userDetails,
                           Model model) {

        List<Message> messageList = messageService.getMessagesInChat(id);
        User chatPartner = messageList.stream().findFirst()
                .map(message -> {
                    User sender = message.getSender();
                    User receiver = message.getReceiver();
                    if (!sender.getUsername().equals(userDetails.getUsername())) {
                        return sender;
                    }
                    return receiver;
                }).orElse(new User());

//        String chatPartnerName = messageList.stream().findFirst()
//                .map(message -> {
//                    User sender = message.getSender();
//                    User receiver = message.getReceiver();
//                    User chatPartner = !sender.getUsername().equals(userDetails.getUsername()) ? sender : receiver;
//                    return chatPartner.getFirstName() + " " + chatPartner.getLastName();
//                })
//                .orElse("Собеседник");

        model.addAttribute("chatId", id);
        model.addAttribute("messages", messageList);
        model.addAttribute("chatPartnerName", chatPartner);
//        model.addAttribute("chatPartnerName", chatPartnerName);

        return "profile/messages/messagesPage";
    }

    @MessageMapping("/sendMessage/{chatId}")
    public void sendMessage(@DestinationVariable Long chatId,
                            @Payload MessageDto messageDto) {

        Message savedMessage = messageService.saveMessage(messageDto);

        brokerMessagingTemplate.convertAndSendToUser(
                savedMessage.getReceiver().getUsername(),
                "/queue/messages/" + chatId,
                messageDto
        );
    }
}
