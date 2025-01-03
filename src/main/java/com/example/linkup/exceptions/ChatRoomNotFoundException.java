package com.example.linkup.exceptions;

public class ChatRoomNotFoundException extends RuntimeException {
    public ChatRoomNotFoundException() {
        super("Чат не найден");
    }
}
