package com.example.linkup.models.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginUserDto {
    @NotNull(message = "Логин не может быть пустым")
    @Size(min = 6, max = 20, message = "Логин должен быть от 6 до 20 символов")
    private String username;

    @NotNull(message = "Пароль не может быть пустым")
    @Size(min = 6, max = 20, message = "Пароль должен быть от 6 до 20 символов")
    private String password;

    private String token;
}
