package com.example.linkup.models.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSearchDto {
    private Long id;
    private String firstName;
    private String lastName;
}
