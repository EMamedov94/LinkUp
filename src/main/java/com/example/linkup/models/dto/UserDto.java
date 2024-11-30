package com.example.linkup.models.dto;

import com.example.linkup.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    @NotNull(message = "Логин не может быть пустым")
    @Size(min = 6, max = 20, message = "Длина логина может быть от 6 до 20 символов")
    private String username;

    @NotNull(message = "Пароль не может быть пустым")
    @Size(min = 6, message = "Длина пароля может быть от 6 до 20 символов")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
