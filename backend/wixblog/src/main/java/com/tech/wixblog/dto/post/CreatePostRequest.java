package com.tech.wixblog.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class CreatePostRequest  {
    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    private String title;
    @Size(max = 300, message = "Excerpt cannot exceed 300 characters")
    private String excerpt;
    @NotBlank(message = "Content is required")
    @Size(min = 100, message = "Content must be at least 100 characters")
    private String content;
    @NotBlank(message = "Category slug is required")
    private String categorySlug;
    @Size(max = 5, message = "Maximum 5 tags allowed")
    private Set<@Size(min = 2, max = 30) String> tags;
    private Set<String> secondaryCategorySlugs;
    private String featuredImage;
    private String metaDescription;
    private String metaKeywords;

}