package com.example.blog.mapper;

import com.example.blog.dto.CommentDtos;
import com.example.blog.model.Comment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-14T11:49:32+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public Comment toEntity(CommentDtos.CreateCommentRequest request) {
        if ( request == null ) {
            return null;
        }

        Comment comment = new Comment();

        comment.setContent( request.getContent() );

        return comment;
    }
}
