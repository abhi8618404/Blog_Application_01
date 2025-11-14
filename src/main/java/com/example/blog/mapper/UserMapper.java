package com.example.blog.mapper;

import com.example.blog.dto.AuthDtos.RegisterRequest;
import com.example.blog.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toEntity(RegisterRequest request);
}


