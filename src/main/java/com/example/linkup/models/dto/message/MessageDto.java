package com.example.linkup.models.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long chatRoomId;
    private Long senderId;
    private Long receiverId;
    private String text;
}
