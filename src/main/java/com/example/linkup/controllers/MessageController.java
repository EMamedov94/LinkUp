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
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
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
        String chatPartnerName = messageList.stream().findFirst()
                .map(message -> {
                    User sender = message.getSender();
                    User receiver = message.getReceiver();
                    User chatPartner = !sender.getUsername().equals(userDetails.getUsername()) ? sender : receiver;
                    return chatPartner.getFirstName() + " " + chatPartner.getLastName();
                })
                .orElse("Собеседник");

        model.addAttribute("chatId", id);
        model.addAttribute("messages", messageList);
        model.addAttribute("chatPartnerName", chatPartnerName);

        return "profile/messages/messagesPage";
    }

    @MessageMapping("/sendMessage/{chatId}")
    @SendTo("/topic/{chatId}")
    public void sendMessage(@DestinationVariable String chatId,
                            Principal principal,
                            MessageDto messageDto) {

        Message savedMessage = messageService.saveMessage(messageDto);

        brokerMessagingTemplate.convertAndSendToUser(
                messageDto.getSenderId().toString(),
                "/queue/messages/" + chatId,
                savedMessage
        );
    }

//    @MessageMapping("/sendMessage/{chatId}")
//    @SendTo("/topic/{chatId}")
//    public void sendMessage(@DestinationVariable String chatId,
//                            @AuthenticationPrincipal UserDetails userDetails,
//                            Message message) {
//        String senderUsername = userDetails.getUsername();
//
//        User sender = new User(senderUsername);
//        User receiver = new User(message.getReceiver().getUsername());
//
//        Message savedMessage = messageService.saveMessage(sender, receiver, message.getText());
//
//        brokerMessagingTemplate.convertAndSendToUser(
//                message.getReceiver().getUsername(),
//                "/queue/messages/" + chatId,
//                savedMessage
//        );
//    }
}
