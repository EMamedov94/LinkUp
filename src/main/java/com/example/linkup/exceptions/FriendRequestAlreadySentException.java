package com.example.linkup.exceptions;

public class FriendRequestAlreadySentException extends RuntimeException {
    public FriendRequestAlreadySentException() {
        super("Заявка в друзья уже была отправлена");
    }
}
