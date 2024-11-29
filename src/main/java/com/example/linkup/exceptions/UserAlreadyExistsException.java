package com.example.linkup.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("Пользователь с таким логином уже существует");
    }
}
