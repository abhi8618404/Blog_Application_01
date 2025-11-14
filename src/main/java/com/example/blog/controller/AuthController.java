package com.example.blog.controller;

import com.example.blog.dto.AuthDtos.LoginRequest;
import com.example.blog.dto.AuthDtos.RegisterRequest;
import com.example.blog.model.User;
import com.example.blog.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class AuthController {
    
    private  UserService userService;
    
    @GetMapping("/login")
    public String loginForm(HttpSession session) {
        if (userService.getCurrentUser(session) != null) {
            return "redirect:/";
        }
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginRequest request,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        try {
            User user = userService.login(request.getUsername(), request.getPassword());
            userService.setUserInSession(session, user);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }
    
    @GetMapping("/register")
    public String registerForm(HttpSession session) {
        if (userService.getCurrentUser(session) != null) {
            return "redirect:/";
        }
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest request,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        try {
            User user = userService.register(request.getUsername(), request.getPassword());
            userService.setUserInSession(session, user);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        userService.logout(session);
        return "redirect:/";
    }
}