package com.example.linkup.services.post.impl;

import com.example.linkup.models.Post;
import com.example.linkup.repositories.PostRepository;
import com.example.linkup.services.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    // Get posts
    @Override
    public Page<Post> getPostsByUserId(Long id) {
        Pageable pageable = PageRequest.of(0, 10);
        return postRepository.findAllByUserId(id, pageable);
    }

    @Override
    public Post getPost(Long id) {
        return null;
    }

    @Override
    public Post savePost(Post post) {
        return null;
    }

    @Override
    public void deletePost(Long id) {

    }
}
