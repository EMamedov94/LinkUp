package com.example.linkup.models.dto.post;

import com.example.linkup.enums.PostStatus;
import com.example.linkup.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewPostDto {
    private String title;
    private String content;
    private User author;
    private PostStatus status;
}
