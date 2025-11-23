package com.tech.wixblog.mapper;


import com.tech.wixblog.dto.LikeDTO;
import com.tech.wixblog.model.Like;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LikeMapper {
    
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "postTitle", source = "post.title")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.firstName")
    LikeDTO toDTO(Like like);
    
    List<LikeDTO> toDTOList(List<Like> likes);
}