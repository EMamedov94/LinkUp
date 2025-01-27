package com.example.linkup.repositories;

import com.example.linkup.models.PostWithLikesCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PostWithLikesCountRepository extends JpaRepository<PostWithLikesCount, Long> {

    PostWithLikesCount findByPostId(Long postId);

    @Query("SELECT COUNT(l) " +
            "FROM Like l " +
            "WHERE l.post.id = :postId " +
            "AND l.status = 'ACTIVE'")
    Long countActiveLikesByPostId(@Param("postId") Long postId);

    @Transactional
    @Modifying
    @Query("UPDATE PostWithLikesCount p " +
            "SET p.likesCount = p.likesCount + :increment " +
            "WHERE p.id = :postId")
    void updateLikesCount(@Param("postId") Long postId, @Param("increment") Long increment);
}
