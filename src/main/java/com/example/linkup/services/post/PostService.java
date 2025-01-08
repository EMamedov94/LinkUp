package com.example.linkup.services.post;

import com.example.linkup.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Page<Post> getPostsByUserId(Long id);
    Post getPost(Long id);
    Post savePost(Post post);
    void deletePost(Long id);
}
