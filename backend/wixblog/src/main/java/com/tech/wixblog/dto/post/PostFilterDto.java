package com.tech.wixblog.dto.post;

import com.tech.wixblog.model.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostFilterDto {
    
    private String categorySlug;
    private String tagSlug;
    private String authorUsername;
    private PostStatus status;
    private Boolean featured;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime publishedAfter;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime publishedBefore;
    
    private String searchQuery;
    private SortBy sortBy;
    private SortDirection sortDirection;
    

    public enum SortBy {
        PUBLISHED_AT, CREATED_AT, UPDATED_AT, VIEWS, LIKES, COMMENTS, READS
    }
    
    public enum SortDirection {
        ASC, DESC
    }
}