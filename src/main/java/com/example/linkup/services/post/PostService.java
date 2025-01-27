package com.example.linkup.services.post;

import com.example.linkup.models.Post;
import com.example.linkup.models.dto.post.NewPostDto;
import com.example.linkup.models.dto.post.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Page<PostDto> getPostsByUserId(Long id);
    Post getPost(Long id);
    void createNewPost(NewPostDto postDto);
    void deletePost(Long id);
}
