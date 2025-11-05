package com.tech.wixblog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {
    private Long id;
    private LocalDateTime createdAt;
    
    // Post information
    private Long postId;
    private String postTitle;
    
    // User information
    private Long userId;
    private String userName;
}