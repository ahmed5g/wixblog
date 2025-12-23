package com.tech.wixblog.repository;

import com.tech.wixblog.model.Tag;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    
    Optional<Tag> findBySlug(String slug);
    


    List<Tag> findAllByOrderByTrendingScoreDesc();
    
    List<Tag> findBySuggestedCategoryId(Long categoryId);

    
    @Query("SELECT t FROM Tag t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%')) ORDER BY t.usageCount DESC")
    List<Tag> searchTags(String query);

    @Modifying
    @Transactional
    @Query("UPDATE Tag t SET t.trendingScore = CAST(t.trendingScore * 0.9 AS int) WHERE t.trendingScore > 0")
    void decayTrendingScores();



    // Tags for user suggestions
    @Query("""
    SELECT t FROM Tag t 
    ORDER BY 
        (CASE WHEN t.suggestedCategory.id IN :categoryIds THEN 1000 ELSE 0 END) + 
        (CASE WHEN t.id IN :tagIds THEN 2000 ELSE 0 END) + 
        t.trendingScore DESC
    """)
    Page<Tag> findSuggestedTagsForUser(
            @Param("tagIds") Set<Long> tagIds,
            @Param("categoryIds") Set<Long> categoryIds,
            Pageable pageable);
    // Trending tags
    @Query("SELECT t FROM Tag t " +
            "WHERE t.trendingScore > 0 " +
            "ORDER BY t.trendingScore DESC")
    List<Tag> findTrendingTags(@Param("limit") int limit);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Tag t SET t.trendingScore = CAST(t.trendingScore * 0.9 AS int) WHERE t.trendingScore > 0")
    void decayAllScores();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Tag t SET t.trendingScore = t.trendingScore + :amount WHERE t.id = :id")
    void incrementScore(@Param("id") Long id, @Param("amount") int amount);
}