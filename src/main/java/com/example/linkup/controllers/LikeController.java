package com.example.linkup.controllers;

import com.example.linkup.models.User;
import com.example.linkup.services.like.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/like/{id}")
    public String like(@AuthenticationPrincipal User currentUser,
                       @PathVariable Long id,
                       HttpServletRequest request) {
        likeService.like(id, currentUser.getId());
        return "redirect:" + request.getHeader("Referer");
    }
}
