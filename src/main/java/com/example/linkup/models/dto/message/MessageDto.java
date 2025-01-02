package com.example.linkup.models.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long chatRoomId;
    private Long senderId;
    private Long receiverId;
    private String text;
    private LocalDateTime timestamp;
}
