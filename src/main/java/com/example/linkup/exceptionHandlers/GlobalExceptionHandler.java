package com.example.linkup.exceptionHandlers;

import com.example.linkup.exceptions.UserAlreadyExistsException;
import com.example.linkup.models.dto.UserDto;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExistsException(UserAlreadyExistsException e, Model model) {
        model.addAttribute("usernameError", e.getMessage());
        model.addAttribute("user", new UserDto());
        return "auth/registrationForm";
    }
}
