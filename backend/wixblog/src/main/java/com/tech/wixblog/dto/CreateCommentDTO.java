package com.tech.wixblog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Create Comment DTO
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDTO {
    private String content;
    private Long parentCommentId; // Optional, for replies
}
