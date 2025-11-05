package com.tech.wixblog.dto;

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
public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isApproved;
    
    // Post information
    private Long postId;
    private String postTitle;
    
    // Author information
    private Long authorId;
    private String authorName;
    private String authorProfilePicture;
    
    // Parent comment for nested comments
    private Long parentCommentId;
    
    // Child comments
    private List<CommentDTO> replies;
}

