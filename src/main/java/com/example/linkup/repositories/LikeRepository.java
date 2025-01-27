package com.example.linkup.repositories;

import com.example.linkup.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByUserIdAndPostId(Long userId, Long postId);

    Long countLikesByPostId(Long postId);
}
