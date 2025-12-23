package com.tech.wixblog.dto.user;

import com.tech.wixblog.model.enums.AuthProvider;
import com.tech.wixblog.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String bio;
    private Role role;
    private Boolean enabled;
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // Business fields
    private LocalDateTime lastLoginAt;
    private LocalDateTime lastActivityAt;
    // Analytics
    private Long totalPosts;
    private Long publishedPosts;
    private Long totalLikesReceived;
    private Long totalCommentsReceived;
    private Long totalViewsReceived;
    private Long engagementRate;
    // Settings
    private Boolean emailNotifications;
    private Boolean commentNotifications;
    private Boolean likeNotifications;
    private Boolean newsletterSubscribed;
    private Boolean showOnlineStatus;
    private Boolean publicProfile;

    @Builder.Default
    private Map<AuthProvider, OAuthProviderInfoDto> linkedProviders = Map.of();

    /*@Pattern(regexp = "^[a-zA-Z0-9\\s,.-]*$", message = "Invalid characters in interests")
    private String interests;*/

   /* private Boolean isFollowing;
    private Boolean isFollowedBy;
    private LocalDateTime followedAt;*/


}