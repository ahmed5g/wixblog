package com.tech.wixblog.dto;

import com.tech.wixblog.model.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Update Post DTO
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostDTO {
    private Long postId;
    private String title;
    private String summary;
    private String content;
    private String slug;
    private String featuredImage;
    private PostStatus status;
    private Boolean allowComments;
    private Boolean isFeatured;
}
