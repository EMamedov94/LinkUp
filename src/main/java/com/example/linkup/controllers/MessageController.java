package com.example.linkup.controllers;

import com.example.linkup.models.Message;
import com.example.linkup.models.User;
import com.example.linkup.services.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/messages/")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate brokerMessagingTemplate;

    @GetMapping("/messagesPage")
    public String messagesPage(Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {
        messageService.getChatRoomsBetweenUsers(userDetails.getUsername(), )
//        String username = userDetails.getUsername();
//
//        model.addAttribute("messages", messageService.getMessagesForUser(username));

        return "profile/messages/messagesPage";
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(@AuthenticationPrincipal UserDetails userDetails,
                            Message message) {
        String senderUsername = userDetails.getUsername();

        User sender = new User(senderUsername);
        User receiver = new User(message.getReceiver().getUsername());

        Message savedMessage = messageService.saveMessage(sender, receiver, message.getText());

        brokerMessagingTemplate.convertAndSendToUser(
                message.getReceiver().getUsername(),
                "/queue/messages",
                savedMessage
        );
    }
}
