package com.tech.wixblog.dto;

public record TagResponse(
        Long id,
    String name,
    String slug,
    Integer usageCount,
    String categoryName
) {}