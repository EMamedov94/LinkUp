package com.example.linkup.exceptions;

public class FriendshipAlreadyExistsException extends RuntimeException {
    public FriendshipAlreadyExistsException() {
        super("Заявка на дружбу уже существует");
    }
}
