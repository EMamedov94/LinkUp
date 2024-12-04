package com.example.linkup.controllers;

import com.example.linkup.config.JwtService;
import com.example.linkup.models.User;
import com.example.linkup.models.dto.UserDto;
import com.example.linkup.services.auth.AuthenticationService;
import com.example.linkup.services.token.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;
    private final JwtService jwtService;

    // Auth form
    @GetMapping("/authPage")
    public String loginForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "auth/authPage";
    }

    // Registration page
    @GetMapping("/registrationPage")
    public String registrationPage(Model model) {
        model.addAttribute("user", new UserDto());
        return "auth/registrationPage";
    }

    // Login
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("user") UserDto user,
                        BindingResult result, HttpServletResponse response) {
        if (result.hasErrors()) {
            return "auth/authPage";
        }

        User loggedInUser = authenticationService.loginUser(user);
        tokenService.addTokenToCookie(loggedInUser, response);

        return "redirect:/profile/profilePage";
    }

    // Registration new user
    @PostMapping("/registration")
    public String registrationNewUser(@ModelAttribute("user") @Valid User userDto,
                                      BindingResult bindingResult,
                                      HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "auth/authPage";
        }

        authenticationService.registrationNewUser(userDto);
        tokenService.addTokenToCookie(userDto, response);

        return "redirect:/auth/registrationPage";
    }

    // Complete registration new user
    @PostMapping("/completeRegistrationForm")
    public String completeRegistrationNewUser(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestParam("firstName") String firstName,
                                              @RequestParam("lastName") String lastName) {

        authenticationService.completeRegistrationNewUser(userDetails, firstName, lastName);

        return "redirect:/auth/authPage";
    }
}
