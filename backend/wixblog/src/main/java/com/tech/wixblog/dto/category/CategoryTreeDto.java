package com.tech.wixblog.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTreeDto {
    
    private Long id;
    private String name;
    private String slug;
    private String iconUrl;
    private Integer level;
    private String fullPath;
    private List<CategoryTreeDto> children;
}