package com.tech.wixblog.mapper;

import com.tech.wixblog.dto.payload.UpdateUserRequest;
import com.tech.wixblog.dto.payload.RegisterRequest;
import com.tech.wixblog.dto.payload.UserResponse;
import com.tech.wixblog.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(target = "engagementRate", expression = "java(user.getEngagementRate())")
    UserResponse userToUserResponse(User user);

    List<UserResponse> usersToUsersResponse (List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "lastActivityAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "oauthProviders", ignore = true)
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "postViews", ignore = true)
    @Mapping(target = "totalPosts", constant = "0L")
    @Mapping(target = "publishedPosts", constant = "0L")
    @Mapping(target = "totalLikesReceived", constant = "0L")
    @Mapping(target = "totalCommentsReceived", constant = "0L")
    @Mapping(target = "totalViewsReceived", constant = "0L")
    @Mapping(target = "emailNotifications", constant = "true")
    @Mapping(target = "commentNotifications", constant = "true")
    @Mapping(target = "likeNotifications", constant = "true")
    @Mapping(target = "newsletterSubscribed", constant = "false")
    @Mapping(target = "publicProfile", constant = "true")
    @Mapping(target = "showOnlineStatus", constant = "true")
    @Mapping(target = "mailSent", ignore = true)
    @Mapping(target = "role", expression = "java(com.tech.wixblog.model.Role.ROLE_USER)")
    @Mapping(target = "enabled", constant = "true")
    User createRequestToUser (RegisterRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "lastActivityAt", ignore = true)
    @Mapping(target = "oauthProviders", ignore = true)
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "postViews", ignore = true)
    @Mapping(target = "totalPosts", ignore = true)
    @Mapping(target = "publishedPosts", ignore = true)
    @Mapping(target = "totalLikesReceived", ignore = true)
    @Mapping(target = "totalCommentsReceived", ignore = true)
    @Mapping(target = "totalViewsReceived", ignore = true)
    @Mapping(target = "mailSent", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    void updateUserFromRequest (UpdateUserRequest request, @MappingTarget User user);


}