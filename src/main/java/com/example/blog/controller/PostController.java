package com.example.blog.controller;

import com.example.blog.dto.PostDtos.CreateOrUpdatePostRequest;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.model.UserRole;
import com.example.blog.service.PostService;
import com.example.blog.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PostController {
    
    private final PostService postService;
    private final UserService userService;
    
    @GetMapping("/posts/{id}")
    public String viewPost(@PathVariable Long id, Model model, HttpSession session) {
        User currentUser = userService.getCurrentUser(session);
        Post post = postService.getVisiblePostById(id, currentUser);
        
        model.addAttribute("post", post);
        model.addAttribute("currentUser", currentUser);
        return "post-detail";
    }
    
    @GetMapping("/posts/new")
    public String newPostForm(HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser(session);
        
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to create a post");
            return "redirect:/login";
        }
        if (currentUser.getRole() == UserRole.READER) {
            redirectAttributes.addFlashAttribute("error", "You are not authorized to create posts");
            return "redirect:/";
        }
        
        return "post-form";
    }
    
    @PostMapping("/posts/new")
    public String createPost(@Valid @ModelAttribute CreateOrUpdatePostRequest request,
                            @RequestParam(name = "image", required = false) MultipartFile image,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser(session);
        
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to create a post");
            return "redirect:/login";
        }
        
        try {
            java.time.LocalDateTime publishTime = null;
            if (request.getPublishedAt() != null && !request.getPublishedAt().isBlank()) {
                publishTime = java.time.LocalDateTime.parse(request.getPublishedAt());
            }
            com.example.blog.model.PostStatus postStatus = null;
            if (request.getStatus() != null && !request.getStatus().isBlank()) {
                postStatus = com.example.blog.model.PostStatus.valueOf(request.getStatus());
            }
            String imageUrl = null;
            if (image != null && !image.isEmpty()) {
                imageUrl = postService.storeImage(image);
            }
            Post post = postService.createPost(request.getTitle(), request.getContent(), currentUser, request.getTags(), postStatus, publishTime);
            if (imageUrl != null) {
                post.setImageUrl(imageUrl);
                postService.save(post);
            }
            return "redirect:/posts/" + post.getId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/posts/new";
        }
    }
    
    @GetMapping("/posts/{id}/edit")
    public String editPostForm(@PathVariable Long id, 
                              Model model, 
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser(session);
        
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to edit a post");
            return "redirect:/login";
        }
        
        try {
            Post post = postService.getPostById(id);
            if (!post.getAuthor().getId().equals(currentUser.getId())) {
                redirectAttributes.addFlashAttribute("error", "You are not authorized to edit this post");
                return "redirect:/posts/" + id;
            }
            model.addAttribute("post", post);
            return "post-edit";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }
    
    @PostMapping("/posts/{id}/edit")
    public String updatePost(@PathVariable Long id,
                            @Valid @ModelAttribute CreateOrUpdatePostRequest request,
                            @RequestParam(name = "image", required = false) MultipartFile image,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser(session);
        
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to edit a post");
            return "redirect:/login";
        }
        
        try {
            java.time.LocalDateTime publishTime = null;
            if (request.getPublishedAt() != null && !request.getPublishedAt().isBlank()) {
                publishTime = java.time.LocalDateTime.parse(request.getPublishedAt());
            }
            com.example.blog.model.PostStatus postStatus = null;
            if (request.getStatus() != null && !request.getStatus().isBlank()) {
                postStatus = com.example.blog.model.PostStatus.valueOf(request.getStatus());
            }
            String imageUrl = null;
            if (image != null && !image.isEmpty()) {
                imageUrl = postService.storeImage(image);
            }
            Post post = postService.updatePost(id, request.getTitle(), request.getContent(), currentUser, request.getTags(), postStatus, publishTime);
            if (imageUrl != null) {
                post.setImageUrl(imageUrl);
                postService.save(post);
            }
            redirectAttributes.addFlashAttribute("success", "Post updated successfully");
            return "redirect:/posts/" + id;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/posts/" + id;
        }
    }
    
    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser(session);
        
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to delete a post");
            return "redirect:/login";
        }
        
        try {
            postService.deletePost(id, currentUser);
            redirectAttributes.addFlashAttribute("success", "Post deleted successfully");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/posts/" + id;
        }
    }
}