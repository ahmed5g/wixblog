package com.tech.wixblog.dto.post;

import com.tech.wixblog.dto.TagResponse;
import com.tech.wixblog.dto.category.CategoryResponseDto;
import com.tech.wixblog.model.enums.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BasePostDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String excerpt;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private PostStatus status;
    private String slug;
    private String featuredImage;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer readTime;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long viewCount = 0L;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long likeCount = 0L;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long commentCount = 0L;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean likedByCurrentUser = false;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
    private CategoryResponseDto category;
    private List<CategoryResponseDto> secondaryCategories;
    // Tags
    private Set<TagResponse> tags;
    // Statistics

   /* private Integer saveCount;
    private Integer shareCount;

    // User-specific data (if authenticated)
    private Boolean isLikedByUser;
    private Boolean isSavedByUser;
    private Boolean isFollowedByUser;

    // SEO
    private String metaDescription;
    private String metaKeywords;*/


}