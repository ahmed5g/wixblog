package com.tech.wixblog.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag extends AuditableEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String slug;


    @ManyToOne
    @JoinColumn(name = "suggested_category_id")
    @JsonBackReference
    private Category suggestedCategory;

    @Column(nullable = false)
    @Builder.Default
    private Integer usageCount = 0;
    
    @Builder.Default
    private boolean featured = false;
    

    
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Post> posts = new HashSet<>();
    

    // For trending tags calculation
    @Builder.Default
    private Integer trendingScore = 0;
    
    public void incrementUsage() {
        this.usageCount++;
        this.trendingScore += 10; // Simple trending algorithm
    }
    
    public void decrementUsage() {
        this.usageCount = Math.max(0, this.usageCount - 1);
    }
}