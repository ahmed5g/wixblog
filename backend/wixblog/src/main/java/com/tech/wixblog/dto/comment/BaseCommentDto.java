package com.tech.wixblog.dto.comment;

import com.tech.wixblog.model.enums.CommentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseCommentDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String content;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private CommentStatus status;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long likeCount;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}