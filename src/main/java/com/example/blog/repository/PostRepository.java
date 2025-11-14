package com.example.blog.repository;

import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.model.Tag;
import com.example.blog.model.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByStatusAndPublishedAtLessThanEqualOrderByCreatedAtDesc(PostStatus status, java.time.LocalDateTime now, Pageable pageable);
    java.util.List<Post> findAllByStatusAndPublishedAtLessThanEqualOrderByCreatedAtDesc(PostStatus status, java.time.LocalDateTime now);
    Page<Post> findByAuthorAndStatusAndPublishedAtLessThanEqualOrderByCreatedAtDesc(User author, PostStatus status, java.time.LocalDateTime now, Pageable pageable);
    Page<Post> findByTagsAndStatusAndPublishedAtLessThanEqualOrderByCreatedAtDesc(Tag tag, PostStatus status, java.time.LocalDateTime now, Pageable pageable);
    Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseAndStatusAndPublishedAtLessThanEqualOrderByCreatedAtDesc(String title, String content, PostStatus status, java.time.LocalDateTime now, Pageable pageable);
}