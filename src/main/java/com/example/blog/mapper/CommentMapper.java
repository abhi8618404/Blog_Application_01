package com.example.blog.mapper;

import com.example.blog.dto.CommentDtos.CreateCommentRequest;
import com.example.blog.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    Comment toEntity(CreateCommentRequest request);
}


