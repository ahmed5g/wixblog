package com.tech.wixblog.mapper;

import com.tech.wixblog.dto.TagResponse;
import com.tech.wixblog.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TagMapper {

    @Mapping(target = "categoryName", source = "suggestedCategory.name")
    TagResponse toResponse(Tag tag);
}