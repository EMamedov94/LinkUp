package com.example.linkup.models.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatRoomDto {
    private Long id;
    private Long companionId;
    private String text;
    private LocalDateTime timestamp;
}
