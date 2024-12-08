package com.example.linkup.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Необходимо авторизоваться");
    }
}
