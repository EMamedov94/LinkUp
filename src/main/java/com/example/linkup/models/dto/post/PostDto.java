package com.example.linkup.models.dto.post;

import com.example.linkup.enums.PostStatus;
import com.example.linkup.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String imageLink;
    private User author;
    private PostStatus status;
    private Long likeCount;
    private LocalDateTime createdAt;
}
