package com.example.linkup.services.like.impl;

import com.example.linkup.enums.LikeStatus;
import com.example.linkup.models.Like;
import com.example.linkup.models.Post;
import com.example.linkup.models.PostWithLikesCount;
import com.example.linkup.models.User;
import com.example.linkup.repositories.LikeRepository;
import com.example.linkup.repositories.PostRepository;
import com.example.linkup.repositories.PostWithLikesCountRepository;
import com.example.linkup.services.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final PostWithLikesCountRepository postWithLikesCountRepository;

    @Transactional
    @Override
    public void like(Long postId, Long userId) {
        // Проверяем наличие существующего лайка
        Like likeDb = likeRepository.findByUserIdAndPostId(userId, postId);

        if (likeDb == null) {
            // Увеличиваем количество лайков
            postWithLikesCountRepository.updateLikesCount(postId, 1L);

            // Создаем новый активный лайк
            likeRepository.save(
                    Like.builder()
                            .user(new User(userId))
                            .post(new Post(postId))
                            .status(LikeStatus.ACTIVE)
                            .build()
            );
        } else if (likeDb.getStatus() == LikeStatus.ACTIVE) {
            // Уменьшаем количество лайков
            postWithLikesCountRepository.updateLikesCount(postId, -1L);

            // Деактивируем существующий лайк
            likeDb.setStatus(LikeStatus.INACTIVE);
            likeRepository.save(likeDb);
        } else {
            // Реактивируем лайк
            postWithLikesCountRepository.updateLikesCount(postId, 1L);

            likeDb.setStatus(LikeStatus.ACTIVE);
            likeRepository.save(likeDb);
        }
    }

    @Override
    public Long countLike(Long postId) {
        return likeRepository.countLikesByPostId(postId);
    }
}
