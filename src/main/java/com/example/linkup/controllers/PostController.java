package com.example.linkup.controllers;

import com.example.linkup.config.GitHubService;
import com.example.linkup.models.User;
import com.example.linkup.models.dto.post.NewPostDto;
import com.example.linkup.services.post.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final GitHubService gitHubService;

    // Create new post
    @PostMapping("/create")
    public String createPost(@ModelAttribute("post") NewPostDto newPost,
                             @AuthenticationPrincipal User currentUser,
                             HttpServletRequest request) {
        String imageUrl = gitHubService.uploadFileToGitHub("images", newPost.getImageUrl().getOriginalFilename(), newPost.getImageUrl());

        newPost.setAuthor(currentUser);
        newPost.setImageLink(imageUrl);
        postService.createNewPost(newPost);


        return "redirect:" + request.getHeader("Referer");
    }

    // Delete post
    @PostMapping("/deletePost/{id}")
    public String deletePost(@PathVariable Long id,
                             HttpServletRequest request) {
        postService.deletePost(id);

        return "redirect:" + request.getHeader("Referer");
    }
}
