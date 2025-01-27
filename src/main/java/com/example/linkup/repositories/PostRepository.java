package com.example.linkup.repositories;

import com.example.linkup.models.Post;
import com.example.linkup.models.dto.post.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT new com.example.linkup.models.dto.post.PostDto(" +
            "p.id, p.title, p.content, p.imageLink, p.author, p.status, COUNT(l), p.createdAt) " +
            "FROM Post p " +
            "LEFT JOIN p.likes l " +
            "WHERE p.author.id = :userId AND p.status = 'CREATED' " +
            "GROUP BY p.id, p.title, p.content, p.imageLink, p.author, p.status, p.createdAt")
    Page<PostDto> finaAllByUserIdAndStatusCreated(@Param("userId") Long userId, Pageable pageable);
}
