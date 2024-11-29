package com.example.linkup.exceptions;

public class InvalidLoginOrPasswordException extends RuntimeException {
    public InvalidLoginOrPasswordException() {
        super("Неверный логин или пароль");
    }
}
