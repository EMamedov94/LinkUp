package com.example.linkup.services.like;

public interface LikeService {
    void like(Long postId, Long userId);
    Long countLike(Long postId);
}
