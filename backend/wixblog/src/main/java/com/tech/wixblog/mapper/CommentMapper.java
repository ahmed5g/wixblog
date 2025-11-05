package com.tech.wixblog.mapper;


import com.tech.wixblog.dto.CommentDTO;
import com.tech.wixblog.dto.CreateCommentDTO;
import com.tech.wixblog.models.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isApproved", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "parentComment", ignore = true)
    Comment toEntity(CreateCommentDTO createCommentDTO);
    
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "postTitle", source = "post.title")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "authorProfilePicture", source = "author.profilePicture")
    @Mapping(target = "parentCommentId", source = "parentComment.id")
    @Mapping(target = "replies", ignore = true)
    CommentDTO toDTO(Comment comment);
    
    List<CommentDTO> toDTOList(List<Comment> comments);
}