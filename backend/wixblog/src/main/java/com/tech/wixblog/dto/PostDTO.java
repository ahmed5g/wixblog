package com.tech.wixblog.dto;

import com.tech.wixblog.model.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private String slug;
    private String featuredImage;
    private PostStatus status;
    private Integer timeToRead;
    private Integer wordCount;
    private Boolean isFeatured;
    private Boolean allowComments;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
    
    // Author information
    private Long authorId;
    private String authorName;
    private String authorProfilePicture;
    
    // For response only
    private Boolean LikedByCurrentUser;
    private Boolean ViewedByCurrentUser;



}

