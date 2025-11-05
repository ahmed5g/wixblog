package com.tech.wixblog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String profilePicture;
    private String role;
    private Boolean enabled;

    // Profile fields
    private String bio;
    private String websiteUrl;
    private String twitterHandle;
    private String githubUsername;
    private String linkedinUrl;
    private String location;
    private String company;
    private String jobTitle;

    // Analytics fields
    private Long totalPosts;
    private Long publishedPosts;
    private Long totalLikesReceived;
    private Long totalCommentsReceived;
    private Long totalViewsReceived;
    private Long engagementRate;

    // Timestamps
    private LocalDateTime joinDate;
    private LocalDateTime lastLoginAt;
    private LocalDateTime lastActivityAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Settings (only included for own profile)
    private Boolean emailNotifications;
    private Boolean commentNotifications;
    private Boolean likeNotifications;
    private Boolean newsletterSubscribed;
    private Boolean publicProfile;
    private Boolean showOnlineStatus;
}

