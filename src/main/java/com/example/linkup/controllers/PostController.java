package com.example.linkup.controllers;

import com.example.linkup.services.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/getPost/{postId}")
    public String getPostById(@PathVariable Long postId, Model model) {
        return "profile/profilePage";
    }
}
