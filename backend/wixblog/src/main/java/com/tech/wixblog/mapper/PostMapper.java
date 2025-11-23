package com.tech.wixblog.mapper;


import com.tech.wixblog.dto.CreatePostDTO;
import com.tech.wixblog.dto.PostDTO;
import com.tech.wixblog.dto.UpdatePostDTO;
import com.tech.wixblog.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "timeToRead", ignore = true)
    @Mapping(target = "wordCount", ignore = true)
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "views", ignore = true)
    Post toEntity(CreatePostDTO createPostDTO);
    
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", source = "author.firstName")
    @Mapping(target = "authorProfilePicture", source = "author.profilePicture")
    @Mapping(target = "LikedByCurrentUser", ignore = true)
    @Mapping(target = "ViewedByCurrentUser", ignore = true)
    PostDTO toDTO(Post post);
    
    List<PostDTO> toDTOList(List<Post> posts);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timeToRead", ignore = true)
    @Mapping(target = "wordCount", ignore = true)
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "views", ignore = true)
    void updateEntityFromDTO(UpdatePostDTO updatePostDTO, @MappingTarget Post post);

}