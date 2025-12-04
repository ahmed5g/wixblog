package com.tech.wixblog.mapper;

import com.tech.wixblog.dto.comment.CommentResponse;
import com.tech.wixblog.dto.comment.CreateCommentRequest;
import com.tech.wixblog.dto.comment.UpdateCommentRequest;
import com.tech.wixblog.model.Comment;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        builder = @Builder(disableBuilder = true))
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "parentComment", ignore = true)
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "status", expression = "java(com.tech.wixblog.model.enums.CommentStatus" +
            ".ACTIVE)")
    Comment toEntity (CreateCommentRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "parentComment", ignore = true)
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromRequest (UpdateCommentRequest request, @MappingTarget Comment comment);

    @Mapping(source = "author", target = "author")
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "parentComment.id", target = "parentCommentId")
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "replyCount", ignore = true)
    CommentResponse toResponse (Comment comment);

    List<CommentResponse> toResponseList (List<Comment> comments);


}