package com.tech.wixblog.dto;

import com.tech.wixblog.model.PostStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Create Post DTO
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostDTO {
    private String title;
    private String summary;
    private String content;
    private String slug;
    private String featuredImage;
    private Boolean allowComments;
    private Boolean isFeatured;
    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.DRAFT;
}
