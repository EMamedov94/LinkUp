package com.example.linkup.services.post.impl;

import com.example.linkup.enums.PostStatus;
import com.example.linkup.models.Post;
import com.example.linkup.models.PostWithLikesCount;
import com.example.linkup.models.dto.post.NewPostDto;
import com.example.linkup.models.dto.post.PostDto;
import com.example.linkup.repositories.PostRepository;
import com.example.linkup.repositories.PostWithLikesCountRepository;
import com.example.linkup.services.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostWithLikesCountRepository postWithLikesCountRepository;

    // Get posts by user id
    @Override
    public Page<PostDto> getPostsByUserId(Long id) {
        Pageable pageable = PageRequest.of(0, 10);
        return postRepository.finaAllByUserIdAndStatusCreated(id, pageable);
    }

    @Override
    public Post getPost(Long id) {
        return null;
    }

    // Create new post
    @Override
    public void createNewPost(NewPostDto postDto) {
        Post newPost = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .author(postDto.getAuthor())
                .imageLink(postDto.getImageLink())
                .createdAt(LocalDateTime.now())
                .status(PostStatus.CREATED)
                .build();

        postRepository.save(newPost);
        postWithLikesCountRepository.save(new PostWithLikesCount(newPost, 0L));
    }

    // Delete post
    @Override
    public void deletePost(Long id) {
        Post postDb = postRepository.findById(id).orElseThrow(RuntimeException::new);
        postDb.setStatus(PostStatus.DELETED);
        postRepository.save(postDb);
    }
}
