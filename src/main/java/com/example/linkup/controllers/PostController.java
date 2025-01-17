package com.example.linkup.controllers;

import com.example.linkup.models.Post;
import com.example.linkup.models.User;
import com.example.linkup.models.dto.post.NewPostDto;
import com.example.linkup.services.post.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    // Create new post
    @PostMapping("/create")
    public String createPost(@ModelAttribute("post") NewPostDto newPost,
                             @AuthenticationPrincipal User currentUser,
                             HttpServletRequest request) {
        newPost.setAuthor(currentUser);
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
