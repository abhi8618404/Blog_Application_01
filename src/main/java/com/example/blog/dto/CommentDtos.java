package com.example.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class CommentDtos {

    @Data
    public static class CreateCommentRequest {
        @NotBlank(message = "Comment content is required")
        private String content;
    }
}


