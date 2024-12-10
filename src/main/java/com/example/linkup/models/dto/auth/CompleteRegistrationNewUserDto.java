package com.example.linkup.models.dto.auth;

import lombok.Data;

@Data
public class CompleteRegistrationNewUserDto {
    //    @Pattern(regexp = "^[А-Яа-я]", message = "Имя должно начинаться с заглавной буквы и быть написано русскими буквами")
    private String firstName;

    //    @Pattern(regexp = "^[А-Яа-я]", message = "Фамилия должно начинаться с заглавной буквы и быть написано русскими буквами")
    private String lastName;
}
