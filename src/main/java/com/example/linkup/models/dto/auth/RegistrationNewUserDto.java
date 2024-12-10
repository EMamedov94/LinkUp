package com.example.linkup.models.dto.auth;

import com.example.linkup.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationNewUserDto {
    @NotNull(message = "Логин не может быть пустым")
    @Size(min = 6, max = 20, message = "Логин должен быть от 6 до 20 символов")
    private String username;

    @NotNull(message = "Пароль не может быть пустым")
    @Size(min = 6, max = 20, message = "Пароль должен быть от 6 до 20 символов")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String token;
}
