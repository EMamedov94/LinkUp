package com.example.linkup.models;

import com.example.linkup.enums.PostStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(exclude = {"comments", "likes"})
@Builder
public class Post extends BaseEntity {
    private String title;
    private String content;
    private String imageLink;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @ManyToOne()
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    public Post(Long id) {
        super(id);
    }
}