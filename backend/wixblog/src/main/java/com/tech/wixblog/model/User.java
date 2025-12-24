package com.tech.wixblog.model;

import com.tech.wixblog.model.enums.AuthProvider;
import com.tech.wixblog.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class  User extends AuditableEntity {
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
    @Lob
    @Column(name = "profile_picture", columnDefinition = "LONGTEXT")
    private String profilePicture;
    @Column(columnDefinition = "TEXT")
    private String bio;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Column(name = "publich_account")
    private Boolean enabled = true;
    // Relationships
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name = "user_follows",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<User> following = new HashSet<>();

    // Followed by other users (inverse relationship)
    @ManyToMany(mappedBy = "following")
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<User> followers = new HashSet<>();

    // Followed categories (simple many-to-many)
    @ManyToMany
    @JoinTable(
            name = "user_followed_categories",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Category> followedCategories = new HashSet<>();

    // Followed tags (simple many-to-many)
    @ManyToMany
    @JoinTable(
            name = "user_followed_tags",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Tag> followedTags = new HashSet<>();



    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;
    // Analytics fields
    @Column(name = "total_posts")
    private Long totalPosts;
    @Column(name = "published_posts")
    private Long publishedPosts;
    @Column(name = "total_likes_received")
    private Long totalLikesReceived;
    @Column(name = "total_comments_received")
    private Long totalCommentsReceived;
    @Column(name = "total_views_received")
    private Long totalViewsReceived;
    // Settings fields
    @Column(name = "email_notifications")
    private Boolean emailNotifications;
    @Column(name = "comment_notifications")
    private Boolean commentNotifications;
    @Column(name = "like_notifications")
    private Boolean likeNotifications;
    @Column(name = "newsletter_subscribed")
    private Boolean newsletterSubscribed;
    @Column(name = "public_profile")
    private Boolean publicProfile;
    @Column(name = "show_online_status")
    private Boolean showOnlineStatus;
    @Column(name = "mail_sent")
    private Boolean mailSent;
    @ElementCollection
    @CollectionTable(
            name = "user_oauth_providers",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "provider")
    @Builder.Default
    private Map<AuthProvider, OAuthProviderInfo> oauthProviders = new HashMap<>();

    public static User createFromOAuth (String email, String name, String firstName,
                                        String lastName, String profilePicture,
                                        AuthProvider provider, String providerUserId) {
        User user = User.builder()
                .email(email)
                .name(name)
                .firstName(firstName)
                .lastName(lastName)
                .profilePicture(profilePicture)
                .oauthProviders(new HashMap<>())
                .role(Role.ROLE_USER)
                .enabled(true)
                .totalPosts(0L)
                .publishedPosts(0L)
                .totalLikesReceived(0L)
                .totalCommentsReceived(0L)
                .totalViewsReceived(0L)
                .emailNotifications(true)
                .commentNotifications(true)
                .likeNotifications(true)
                .newsletterSubscribed(false)
                .publicProfile(true)
                .showOnlineStatus(true)
                .build();
        // Add the OAuth provider info
        user.addOAuthProvider(provider, providerUserId);
        return user;
    }

    public static OAuthProviderInfo create (String providerUserId) {
        LocalDateTime now = LocalDateTime.now();
        return new OAuthProviderInfo(providerUserId, now, now);
    }

    public void addOAuthProvider (AuthProvider provider, String providerUserId) {
        OAuthProviderInfo providerInfo = OAuthProviderInfo.create(providerUserId);
        this.oauthProviders.put(provider, providerInfo);
    }

    public void updateOAuthProviderUsage (AuthProvider provider) {
        OAuthProviderInfo providerInfo = this.oauthProviders.get(provider);
        if (providerInfo != null) {
            providerInfo.updateLastUsed();
        }
        this.recordLogin();
    }

    public boolean hasOAuthProvider (AuthProvider provider) {
        return this.oauthProviders.containsKey(provider);
    }

    public String getOAuthProviderId (AuthProvider provider) {
        OAuthProviderInfo providerInfo = this.oauthProviders.get(provider);
        return providerInfo != null ? providerInfo.getProviderUserId() : null;
    }

    public List<AuthProvider> getLinkedProviders () {
        return new ArrayList<>(oauthProviders.keySet());
    }

    public void removeOAuthProvider (AuthProvider provider) {
        // Prevent removing last authentication method if no password
        if (!this.hasPassword() && this.oauthProviders.size() <= 1) {
            throw new IllegalStateException(
                    "Cannot remove last authentication method for user without password");
        }
        this.oauthProviders.remove(provider);
    }

    public void updateFromOAuthProfile (String name, String firstName,
                                        String lastName, String profilePicture) {
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.recordLogin();
    }

    public boolean hasPassword () {
        return password != null && !password.trim().isEmpty();
    }

    @PrePersist
    public void prePersist () {
        if (this.lastActivityAt == null) this.lastActivityAt = LocalDateTime.now();
        if (this.role == null) this.role = Role.ROLE_GUEST;
    }

    // Helper methods
    public void incrementTotalPosts () {
        this.totalPosts = this.totalPosts != null ? this.totalPosts + 1 : 1;
        updateLastActivity();
    }

    public void decrementTotalPosts () {
        this.totalPosts = this.totalPosts != null && this.totalPosts > 0 ? this.totalPosts - 1 : 0;
        updateLastActivity();
    }

    public void incrementPublishedPosts () {
        this.publishedPosts = this.publishedPosts != null ? this.publishedPosts + 1 : 1;
        updateLastActivity();
    }

    public void decrementPublishedPosts () {
        this.publishedPosts = this.publishedPosts != null && this.publishedPosts > 0 ? this.publishedPosts - 1 : 0;
        updateLastActivity();
    }

    public void addLikesReceived (Long count) {
        this.totalLikesReceived = this.totalLikesReceived != null ? this.totalLikesReceived + count : count;
        updateLastActivity();
    }

    public void removeLikesReceived (Long count) {
        this.totalLikesReceived = this.totalLikesReceived != null && this.totalLikesReceived >= count ?
                this.totalLikesReceived - count : 0;
        updateLastActivity();
    }

    public void addCommentsReceived (Long count) {
        this.totalCommentsReceived = this.totalCommentsReceived != null ? this.totalCommentsReceived + count : count;
        updateLastActivity();
    }

    public void removeCommentsReceived (Long count) {
        this.totalCommentsReceived = this.totalCommentsReceived != null && this.totalCommentsReceived >= count ?
                this.totalCommentsReceived - count : 0;
        updateLastActivity();
    }

    public void addViewsReceived (Long count) {
        this.totalViewsReceived = this.totalViewsReceived != null ? this.totalViewsReceived + count : count;
        updateLastActivity();
    }

    public void recordLogin () {
        this.lastLoginAt = LocalDateTime.now();
        updateLastActivity();
    }

    private void updateLastActivity () {
        this.lastActivityAt = LocalDateTime.now();
    }
    // Utility methods

    public Long getEngagementRate () {
        if (this.publishedPosts == null || this.publishedPosts == 0) {
            return 0L;
        }
        long totalEngagement = (this.totalLikesReceived != null ? this.totalLikesReceived : 0) +
                (this.totalCommentsReceived != null ? this.totalCommentsReceived : 0);
        return totalEngagement / this.publishedPosts;
    }

    public boolean isAdmin () {
        return this.role == Role.ROLE_ADMIN;
    }




    // ========== HELPER METHODS ==========

    public void followUser(User userToFollow) {
        if (!this.equals(userToFollow)) {
            this.following.add(userToFollow);
            userToFollow.getFollowers().add(this);
        }
    }

    public void unfollowUser(User userToUnfollow) {
        this.following.remove(userToUnfollow);
        userToUnfollow.getFollowers().remove(this);
    }

    public void followCategory(Category category) {
        this.followedCategories.add(category);
    }

    public void unfollowCategory(Category category) {
        this.followedCategories.remove(category);
    }

    public void followTag(Tag tag) {
        this.followedTags.add(tag);
    }

    public void unfollowTag(Tag tag) {
        this.followedTags.remove(tag);
    }

    public boolean isFollowingUser(User user) {
        return this.following.contains(user);
    }

    public boolean isFollowingCategory(Category category) {
        return this.followedCategories.contains(category);
    }

    public boolean isFollowingTag(Tag tag) {
        return this.followedTags.contains(tag);
    }

    public int getFollowingCount() {
        return this.following.size();
    }

    public int getFollowerCount() {
        return this.followers.size();
    }
}