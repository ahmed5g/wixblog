package com.tech.wixblog.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //User Infos
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password")
    private String password;


    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;




    private String profilePicture;


    @Transient
    private Map<String, Object> attributes;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role ;



    @Column(nullable = false)
    private Boolean enabled = true;



    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostView> postViews = new ArrayList<>();

    @Column(name = "join_date")
    private LocalDateTime joinDate;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;




    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "twitter_handle")
    private String twitterHandle;

    @Column(name = "github_username")
    private String githubUsername;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "location")
    private String location;

    @Column(name = "company")
    private String company;

    @Column(name = "job_title")
    private String jobTitle;

    // OAuth account (nullable for LOCAL users)
    @Embedded
    private OAuthAccount oauth;

    // Analytics fields
    @Column(name = "total_posts")
    private Long totalPosts = 0L;

    @Column(name = "published_posts")
    private Long publishedPosts = 0L;

    @Column(name = "total_likes_received")
    private Long totalLikesReceived = 0L;

    @Column(name = "total_comments_received")
    private Long totalCommentsReceived = 0L;

    @Column(name = "total_views_received")
    private Long totalViewsReceived = 0L;



    // Settings fields
    @Column(name = "email_notifications")
    private Boolean emailNotifications = true;

    @Column(name = "comment_notifications")
    private Boolean commentNotifications = true;

    @Column(name = "like_notifications")
    private Boolean likeNotifications = true;

    @Column(name = "newsletter_subscribed")
    private Boolean newsletterSubscribed = false;

    @Column(name = "public_profile")
    private Boolean publicProfile = true;

    @Column(name = "show_online_status")
    private Boolean showOnlineStatus = true;

    @Column(name = "mail_sent")
    private Boolean mailSent;





    public boolean hasPassword() {
        return password != null && !password.trim().isEmpty();
    }

    // Domain behavior
    public void changeOAuthAccount(OAuthAccount newAccount) {
        if (newAccount == null) {
            // clearing oauth: becomes LOCAL
            this.oauth = null;
            return;
        }
        newAccount.validate();
        this.oauth = newAccount;
    }


    @PrePersist
    public void prePersist() {
        if (this.joinDate == null) {
            this.joinDate = LocalDateTime.now();
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
        if (this.lastActivityAt == null) {
            this.lastActivityAt = LocalDateTime.now();
        }

        if (this.role == null) {
            this.role = Role.ROLE_USER;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Helper methods
    public void incrementTotalPosts() {
        this.totalPosts = this.totalPosts != null ? this.totalPosts + 1 : 1;
        updateLastActivity();
    }

    public void decrementTotalPosts() {
        this.totalPosts = this.totalPosts != null && this.totalPosts > 0 ? this.totalPosts - 1 : 0;
        updateLastActivity();
    }

    public void incrementPublishedPosts() {
        this.publishedPosts = this.publishedPosts != null ? this.publishedPosts + 1 : 1;
        updateLastActivity();
    }

    public void decrementPublishedPosts() {
        this.publishedPosts = this.publishedPosts != null && this.publishedPosts > 0 ? this.publishedPosts - 1 : 0;
        updateLastActivity();
    }

    public void addLikesReceived(Long count) {
        this.totalLikesReceived = this.totalLikesReceived != null ? this.totalLikesReceived + count : count;
        updateLastActivity();
    }

    public void removeLikesReceived(Long count) {
        this.totalLikesReceived = this.totalLikesReceived != null && this.totalLikesReceived >= count ?
                this.totalLikesReceived - count : 0;
        updateLastActivity();
    }

    public void addCommentsReceived(Long count) {
        this.totalCommentsReceived = this.totalCommentsReceived != null ? this.totalCommentsReceived + count : count;
        updateLastActivity();
    }

    public void removeCommentsReceived(Long count) {
        this.totalCommentsReceived = this.totalCommentsReceived != null && this.totalCommentsReceived >= count ?
                this.totalCommentsReceived - count : 0;
        updateLastActivity();
    }

    public void addViewsReceived(Long count) {
        this.totalViewsReceived = this.totalViewsReceived != null ? this.totalViewsReceived + count : count;
        updateLastActivity();
    }

    public void recordLogin() {
        this.lastLoginAt = LocalDateTime.now();
        updateLastActivity();
    }





    private void updateLastActivity() {
        this.lastActivityAt = LocalDateTime.now();
    }

    // Utility methods
    public boolean isAdmin() {
        return this.role == Role.ROLE_ADMIN;
    }

    public boolean isActive() {
        return this.enabled && this.publicProfile;
    }


    public Long getEngagementRate() {
        if (this.publishedPosts == null || this.publishedPosts == 0) {
            return 0L;
        }
        long totalEngagement = (this.totalLikesReceived != null ? this.totalLikesReceived : 0) +
                (this.totalCommentsReceived != null ? this.totalCommentsReceived : 0);
        return totalEngagement / this.publishedPosts;
    }


}