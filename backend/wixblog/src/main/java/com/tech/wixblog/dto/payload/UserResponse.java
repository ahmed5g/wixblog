package com.tech.wixblog.dto.payload;

import com.tech.wixblog.model.Role;
import com.tech.wixblog.model.enums.AuthProvider;
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
    private Map<AuthProvider, OAuthProviderInfo> oauthProviders = Map.of();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OAuthProviderInfo {
        private String providerUserId;
        private LocalDateTime linkedAt;
        private LocalDateTime lastUsedAt;
    }
}