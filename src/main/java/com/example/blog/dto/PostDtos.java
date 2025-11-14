package com.example.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@SuppressWarnings("unused")
public class PostDtos {

    @Data
    public static class CreateOrUpdatePostRequest {
        @NotBlank(message = "Title is required")
        private String title;

        @NotBlank(message = "Content is required")
        private String content;

        // comma-separated tag names
        private String tags;

        // DRAFT, PUBLISHED, SCHEDULED
        private String status;

        // ISO-8601 string, e.g., 2024-01-01T10:00:00
        private String publishedAt;

        // optional image url when using non-multipart form posts
        private String imageUrl;
    }
}


