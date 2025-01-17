package com.example.linkup.repositories;

import com.example.linkup.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p " +
            "WHERE p.author.id = :userId AND p.status = 'CREATED' " +
            "ORDER BY p.createdAt DESC ")
    Page<Post> findAllByUserIdAndStatusCreated(@Param("userId") Long userId, Pageable pageable);
}
