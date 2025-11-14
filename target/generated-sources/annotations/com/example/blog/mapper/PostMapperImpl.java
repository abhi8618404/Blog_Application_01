package com.example.blog.mapper;

import com.example.blog.dto.PostDtos;
import com.example.blog.model.Post;
import com.example.blog.model.PostStatus;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-14T11:49:32+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public Post toEntity(PostDtos.CreateOrUpdatePostRequest request) {
        if ( request == null ) {
            return null;
        }

        Post post = new Post();

        post.setTitle( request.getTitle() );
        post.setContent( request.getContent() );
        post.setTags( map( request.getTags() ) );
        post.setImageUrl( request.getImageUrl() );
        if ( request.getStatus() != null ) {
            post.setStatus( Enum.valueOf( PostStatus.class, request.getStatus() ) );
        }
        if ( request.getPublishedAt() != null ) {
            post.setPublishedAt( LocalDateTime.parse( request.getPublishedAt() ) );
        }

        return post;
    }
}
