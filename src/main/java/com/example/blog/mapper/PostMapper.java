package com.example.blog.mapper;

import com.example.blog.dto.PostDtos.CreateOrUpdatePostRequest;
import com.example.blog.model.Post;
import com.example.blog.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper {

    Post toEntity(CreateOrUpdatePostRequest request);

    // Custom conversion method for String -> Set<Tag>
    default Set<Tag> map(String tags) {
        if (tags == null || tags.isBlank()) {
            return Collections.emptySet();
        }

        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Tag::new) // assumes Tag has a constructor: Tag(String name)
                .collect(Collectors.toSet());
    }
}
