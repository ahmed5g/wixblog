package com.tech.wixblog.dto.post;

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
public class UpdatePostRequest extends BasePostDto {
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    private String title;
    @Size(min = 10, message = "Content must be at least 10 characters")
    private String content;
    @Size(max = 500, message = "Summary must not exceed 500 characters")
    private String summary;
}