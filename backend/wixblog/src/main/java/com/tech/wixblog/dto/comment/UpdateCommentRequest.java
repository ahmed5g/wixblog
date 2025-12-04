package com.tech.wixblog.dto.comment;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateCommentRequest extends BaseCommentDto {
    
    @Size(min = 1, max = 1000, message = "Content must be between 1 and 1000 characters")
    private String content;
}