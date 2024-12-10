package com.example.linkup.exceptions;

public class FriendshipNotFoundException extends RuntimeException {
    public FriendshipNotFoundException() {
        super("Дружба не найдена");
    }
}
