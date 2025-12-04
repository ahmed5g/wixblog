package com.tech.wixblog.model;

import com.tech.wixblog.model.enums.PostStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status = PostStatus.DRAFT;

    @Column(name = "slug", unique = true)
    private String slug;

    @Column(name = "featured_image")
    private String featuredImage;

    @Column(name = "read_time")
    private Integer readTime;

    @Column(name = "view_count")
    @Builder.Default
    private Long viewCount = 0L;

    @Column(name = "like_count")
    @Builder.Default
    private Long likeCount = 0L;

    @Column(name = "comment_count")
    @Builder.Default
    private Long commentCount = 0L;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "user_id"})
    )
    @Builder.Default
    private Set<User> likedByUsers = new HashSet<>();

    // Helper methods
    public void incrementViewCount() {
        this.viewCount = (this.viewCount == null) ? 1 : this.viewCount + 1;
    }

    public void incrementLikeCount() {
        this.likeCount = (this.likeCount == null) ? 1 : this.likeCount + 1;
    }

    public void decrementLikeCount() {
        this.likeCount = (this.likeCount == null || this.likeCount <= 0) ? 0 : this.likeCount - 1;
    }

    public void incrementCommentCount() {
        this.commentCount = (this.commentCount == null) ? 1 : this.commentCount + 1;
    }

    public void decrementCommentCount() {
        this.commentCount = (this.commentCount == null || this.commentCount <= 0) ? 0 : this.commentCount - 1;
    }

    public boolean toggleLike(User user) {
        if (likedByUsers.contains(user)) {
            likedByUsers.remove(user);
            decrementLikeCount();
            return false;
        } else {
            likedByUsers.add(user);
            incrementLikeCount();
            return true;
        }
    }

    public boolean isLikedByUser(User user) {
        return likedByUsers != null && likedByUsers.contains(user);
    }

    public boolean isLikedByUserId(Long userId) {
        if (likedByUsers == null || likedByUsers.isEmpty()) return false;
        return likedByUsers.stream()
                .anyMatch(user -> user.getId().equals(userId));
    }

    public boolean isPublished() {
        return this.status == PostStatus.PUBLISHED;
    }

    public boolean canBeModifiedBy(User user) {
        return this.author.getId().equals(user.getId()) || user.isAdmin();
    }


    public Long getViewCount() {
        return viewCount != null ? viewCount : 0L;
    }

    public Long getLikeCount() {
        return likeCount != null ? likeCount : 0L;
    }

    public Long getCommentCount() {
        return commentCount != null ? commentCount : 0L;
    }
}