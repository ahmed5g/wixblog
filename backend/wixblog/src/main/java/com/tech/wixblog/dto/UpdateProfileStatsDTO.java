package com.tech.wixblog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileStatsDTO {
    private Long postsDelta,
            publishedPostsDelta,
            likesDelta,
            commentsDelta, viewsDelta;
}

