package com.tech.wixblog.mapper;


import com.tech.wixblog.dto.PostViewDTO;
import com.tech.wixblog.model.PostView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostViewMapper {
    
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "postTitle", source = "post.title")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.firstName")
    PostViewDTO toDTO(PostView postView);
    
    List<PostViewDTO> toDTOList(List<PostView> postViews);
}