package com.example.blog.service;

import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.model.UserRole;
import com.example.blog.model.Tag;
import com.example.blog.model.PostStatus;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final FileStorageService fileStorageService;
    
    @Cacheable(cacheNames = "postsPage", key = "#page + '-' + #size + '-' + (#q == null ? '' : #q)")
    public Page<Post> getPostsPage(int page, int size, String q) {
        var pageable = PageRequest.of(page, size);
        var now = java.time.LocalDateTime.now();
        if (q != null && !q.isBlank()) {
            return postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseAndStatusAndPublishedAtLessThanEqualOrderByCreatedAtDesc(q, q, PostStatus.PUBLISHED, now, pageable);
        }
        return postRepository.findAllByStatusAndPublishedAtLessThanEqualOrderByCreatedAtDesc(PostStatus.PUBLISHED, now, pageable);
    }

    @Cacheable(cacheNames = "postsPageByTag", key = "#page + '-' + #size + '-' + (#q == null ? '' : #q) + '-' + (#tagName == null ? '' : #tagName)")
    public Page<Post> getPostsPage(int page, int size, String q, String tagName) {
        var pageable = PageRequest.of(page, size);
        var now = java.time.LocalDateTime.now();
        if (tagName != null && !tagName.isBlank()) {
            var tag = tagRepository.findByNameIgnoreCase(tagName).orElse(null);
            if (tag == null) {
                return Page.empty(pageable);
            }
            return postRepository.findByTagsAndStatusAndPublishedAtLessThanEqualOrderByCreatedAtDesc(tag, PostStatus.PUBLISHED, now, pageable);
        }
        return getPostsPage(page, size, q);
    }
    
    @Cacheable(cacheNames = "postById", key = "#id")
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    public Post getVisiblePostById(Long id, User currentUser) {
        Post post = getPostById(id);
        boolean isAuthor = currentUser != null && post.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser != null && currentUser.getRole() == UserRole.ADMIN;
        boolean isPublished = post.getStatus() == PostStatus.PUBLISHED && !post.getPublishedAt().isAfter(java.time.LocalDateTime.now());
        if (isPublished || isAuthor || isAdmin) {
            return post;
        }
        throw new IllegalArgumentException("Post not available");
    }
    
    public Post createPost(String title, String content, User author) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        return postRepository.save(post);
    }

    @CacheEvict(cacheNames = {"postsPage", "postsPageByTag"}, allEntries = true)
    public Post createPost(String title, String content, User author, String tagsCsv, PostStatus status, java.time.LocalDateTime publishedAt) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        post.setStatus(status != null ? status : PostStatus.PUBLISHED);
        if (publishedAt != null) {
            post.setPublishedAt(publishedAt);
        }
        if (tagsCsv != null && !tagsCsv.isBlank()) {
            post.setTags(resolveTags(tagsCsv));
        }
        return postRepository.save(post);
    }
    
    public Post updatePost(Long id, String title, String content, User currentUser) {
        Post post = getPostById(id);
        
        // Check if the current user is the author
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You are not authorized to edit this post");
        }
        
        post.setTitle(title);
        post.setContent(content);
        return postRepository.save(post);
    }

    @CacheEvict(cacheNames = {"postsPage", "postsPageByTag", "postById"}, allEntries = true)
    public Post updatePost(Long id, String title, String content, User currentUser, String tagsCsv, PostStatus status, java.time.LocalDateTime publishedAt) {
        Post post = getPostById(id);
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You are not authorized to edit this post");
        }
        post.setTitle(title);
        post.setContent(content);
        if (tagsCsv != null) {
            post.setTags(resolveTags(tagsCsv));
        }
        if (status != null) {
            post.setStatus(status);
        }
        if (publishedAt != null) {
            post.setPublishedAt(publishedAt);
        }
        return postRepository.save(post);
    }

    private Set<Tag> resolveTags(String tagsCsv) {
        var names = List.of(tagsCsv.split(","))
                .stream()
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toSet());
        return names.stream()
                .map(n -> tagRepository.findByNameIgnoreCase(n).orElseGet(() -> {
                    Tag t = new Tag();
                    t.setName(n);
                    return tagRepository.save(t);
                }))
                .collect(Collectors.toSet());
    }
    
    @CacheEvict(cacheNames = {"postsPage", "postsPageByTag", "postById"}, allEntries = true)
    public void deletePost(Long id, User currentUser) {
        Post post = getPostById(id);
        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;
        if (!post.getAuthor().getId().equals(currentUser.getId()) && !isAdmin) {
            throw new IllegalArgumentException("You are not authorized to delete this post");
        }
        
        postRepository.delete(post);
    }
    
    public Page<Post> getPostsByUser(User user, int page, int size) {
        var pageable = PageRequest.of(page, size);
        var now = java.time.LocalDateTime.now();
        return postRepository.findByAuthorAndStatusAndPublishedAtLessThanEqualOrderByCreatedAtDesc(user, PostStatus.PUBLISHED, now, pageable);
    }

    public String storeImage(MultipartFile image) {
        return fileStorageService.storeImage(image);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }
}