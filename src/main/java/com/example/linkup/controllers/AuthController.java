package com.example.linkup.controllers;

import com.example.linkup.models.dto.auth.CompleteRegistrationNewUserDto;
import com.example.linkup.models.dto.auth.LoginUserDto;
import com.example.linkup.models.dto.auth.RegistrationNewUserDto;
import com.example.linkup.services.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
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

    // Auth form
    @GetMapping("/authPage")
    public String loginForm(Model model) {
        model.addAttribute("user", new LoginUserDto());
        return "auth/authPage";
    }

    // Registration page
    @GetMapping("/registrationPage")
    public String registrationPage(Model model) {
        model.addAttribute("user", new RegistrationNewUserDto());
        return "auth/registrationPage";
    }

    // Login
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("user") LoginUserDto userDto,
                        BindingResult result, HttpServletResponse response) {
        if (result.hasErrors()) {
            return "auth/authPage";
        }

        Long userDbId = authenticationService.loginUser(userDto, response).getId();

        return "redirect:/profile/profilePage/" + userDbId;
    }

    // Registration new user
    @PostMapping("/registration")
    public String registrationNewUser(@ModelAttribute("user") @Valid RegistrationNewUserDto userDto,
                                      BindingResult bindingResult,
                                      HttpServletResponse response) {
        if (bindingResult.hasFieldErrors("username") || bindingResult.hasFieldErrors("password")) {
            return "auth/authPage";
        }

        authenticationService.registrationNewUser(userDto, response);

        return "redirect:/auth/registrationPage";
    }

    // Complete registration new user
    @PostMapping("/completeRegistrationForm")
    public String completeRegistrationNewUser(@AuthenticationPrincipal UserDetails userDetails,
                                              CompleteRegistrationNewUserDto userDto) {

        authenticationService.completeRegistrationNewUser(userDetails, userDto);

        return "redirect:/auth/authPage";
    }
}
