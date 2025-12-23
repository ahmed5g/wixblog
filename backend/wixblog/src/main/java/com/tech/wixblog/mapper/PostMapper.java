package com.tech.wixblog.mapper;

import com.tech.wixblog.dto.post.CreatePostRequest;
import com.tech.wixblog.dto.post.PostResponse;
import com.tech.wixblog.dto.post.UpdatePostRequest;
import com.tech.wixblog.model.Post;
import com.tech.wixblog.security.UserPrincipal;
import org.mapstruct.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        builder = @Builder(disableBuilder = true))
public interface PostMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "likedByUsers", ignore = true)
    @Mapping(target = "viewCount", constant = "0L")
    @Mapping(target = "likeCount", constant = "0L")
    @Mapping(target = "commentCount", constant = "0L")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "status", expression = "java(com.tech.wixblog.model.enums.PostStatus.DRAFT)")
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "tags", ignore = true)
    public abstract Post toEntity (CreatePostRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "likedByUsers", ignore = true)
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "likeCount", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "tags", ignore = true)
    public abstract void updateEntityFromRequest (UpdatePostRequest request,
                                                  @MappingTarget Post post);

    @Mapping(source = "author", target = "author")
    @Mapping(target = "likedByCurrentUser", expression = "java(isLikedByCurrentUser(post))")
    public abstract PostResponse toResponse (Post post);

    default String generateSlug (String title) {
        if (title == null) return null;
        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
    }

    @AfterMapping
    default void afterMapping (@MappingTarget Post post, CreatePostRequest request) {
        if (request.getTitle() != null && post.getSlug() == null) {
            post.setSlug(generateSlug(request.getTitle()));
        }
        if (request.getContent() != null) {
            post.setReadTime(calculateReadTime(request.getContent()));
        }
        if (post.getLikedByUsers() == null) {
            post.setLikedByUsers(new HashSet<>());
        }
    }

    @AfterMapping
    default void afterUpdateMapping (@MappingTarget Post post, UpdatePostRequest request) {
        if (request.getTitle() != null && !request.getTitle().equals(post.getTitle())) {
            post.setSlug(generateSlug(request.getTitle()));
        }
        if (request.getContent() != null && !request.getContent().equals(post.getContent())) {
            post.setReadTime(calculateReadTime(request.getContent()));
        }
    }

    private int calculateReadTime (String content) {
        int wordsPerMinute = 200;
        String plainText = content.replaceAll("<[^>]*>", "");
        int wordCount = plainText.split("\\s+").length;
        return Math.max(1, wordCount / wordsPerMinute);
    }

    default Boolean isLikedByCurrentUser (Post post) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserPrincipal) {
                Long currentUserId = ((UserPrincipal) principal).getId();
                return post.isLikedByUserId(currentUserId);
            }
            return false;
        } catch (Exception e) {
            return false; // If no authenticated user or error
        }
    }
}