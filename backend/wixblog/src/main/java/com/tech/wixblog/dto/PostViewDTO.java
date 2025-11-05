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
public class PostViewDTO {
    private Long id;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime viewedAt;
    
    // Post information
    private Long postId;
    private String postTitle;
    
    // User information (if available)
    private Long userId;
    private String userName;
}