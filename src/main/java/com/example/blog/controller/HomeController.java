package com.example.blog.controller;

import com.example.blog.model.User;
import com.example.blog.service.PostService;
import com.example.blog.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final PostService postService;
    private final UserService userService;
    
    @GetMapping("/")
    public String home(Model model, HttpSession session, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "5") int size, @RequestParam(name = "q", required = false) String q, @RequestParam(name = "tag", required = false) String tag) {
        User currentUser = userService.getCurrentUser(session);
        Page<?> postPage = postService.getPostsPage(page, size, q, tag);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("postPage", postPage);
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("q", q);
        model.addAttribute("tag", tag);
        return "index";
    }
}