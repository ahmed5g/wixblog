package com.tech.wixblog.dto.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponseDto {

    private Long id;
    private String name;
    private String slug;
    private String description;
    //todo remove this one
    private String iconUrl;
    private Integer displayOrder;
    private boolean featured;
    private Long parentId;
    private String parentName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // For tree structure
    private List<CategoryResponseDto> children;
    
    // Statistics
    private Integer postCount;
    private Integer followerCount;
}