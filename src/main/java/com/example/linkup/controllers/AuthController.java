package com.example.linkup.controllers;

import com.example.linkup.exceptions.UserAlreadyExistsException;
import com.example.linkup.models.dto.UserDto;
import com.example.linkup.services.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    // Login form
    @GetMapping("/auth")
    public String loginForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "auth/auth";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("user") UserDto user,
                        BindingResult result) {
        if (result.hasErrors()) {
            return "auth/loginForm";
        }
        authenticationService.loginUser(user);
        return "redirect:/";
    }

    // Registration form
    @GetMapping("/registrationForm")
    public String registrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "auth/registrationForm";
    }

    // Registration new user
    @PostMapping("/registration")
    public String registrationNewUser(@ModelAttribute("user") @Valid UserDto userDto,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/registrationForm";
        }
        authenticationService.registrationNewUser(userDto);
        return "redirect:/loginForm";
    }
}
