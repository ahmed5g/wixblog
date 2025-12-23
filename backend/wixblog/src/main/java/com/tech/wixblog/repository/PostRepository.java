package com.tech.wixblog.repository;

import com.tech.wixblog.model.Category;
import com.tech.wixblog.model.Post;
import com.tech.wixblog.model.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    Optional<Post> findByIdAndStatus (Long id, PostStatus status);

    Optional<Post> findBySlugAndStatus (String slug, PostStatus status);

    Page<Post> findByStatus (PostStatus status, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.author.id = :authorId")
    Page<Post> findAllByAuthorId (@Param("authorId") Long authorId, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.author.id = :userId")
    Long countTotalPostsByAuthorId (@Param("userId") Long userId);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.author.id = :userId AND p.status = :status")
    Long countByAuthorIdAndStatus (@Param("userId") Long userId,
                                   @Param("status") PostStatus status);

    @Query("SELECT p FROM Post p WHERE p.status = :status AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.content) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.summary) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Post> searchPublishedPosts (@Param("query") String query,
                                     @Param("status") PostStatus status,
                                     Pageable pageable);



    Optional<Post> findBySlug(String slug);


    Page<Post> findByCategorySlugAndStatus (
            String categorySlug,
            PostStatus status,
            Pageable pageable
                                                   );

    Page<Post> findByCategorySlugAndStatusAndTagsSlugIn(
            String categorySlug,
            PostStatus status,
            List<String> tagSlugs,
            Pageable pageable
                                                       );

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.slug = :tagSlug AND p.status = :status")
    Page<Post> findByTagSlugAndStatus(
            @Param("tagSlug") String tagSlug,
            @Param("status") PostStatus status,
            Pageable pageable
                                     );

    @Query("SELECT p FROM Post p WHERE p.status = 'PUBLISHED' AND p.category.id = :categoryId " +
            "ORDER BY p.createdAt DESC")
    List<Post> findRecentPostsByCategory(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.id = :tagId AND p.status = 'PUBLISHED'")
    Page<Post> findPostsByTagId(@Param("tagId") Long tagId, Pageable pageable);

    List<Post> findByCreatedAtBetweenAndStatus(
            LocalDateTime start,
            LocalDateTime end,
            PostStatus status
                                                );


    // Basic queries

//todo change createdAt to publishedAt
// In PostRepository.java
@Query("SELECT p FROM Post p " +
        "WHERE p.status = 'PUBLISHED' " +
        "AND (" +
        "  p.author.id IN :userIds " +
        "  OR p.category.id IN :categoryIds " +
        "  OR EXISTS (SELECT t FROM p.tags t WHERE t.id IN :tagIds)" +
        ") " +
        "ORDER BY p.createdAt DESC")
Page<Post> findPostsForFeed(@Param("userIds") Set<Long> userIds,
                            @Param("categoryIds") Set<Long> categoryIds,
                            @Param("tagIds") Set<Long> tagIds,
                            Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "WHERE p.status = 'PUBLISHED' " +
            "AND p.createdAt >= :since " +
            "ORDER BY (p.viewCount + p.likeCount * 2 + p.commentCount * 3) DESC")
    Page<Post> findPopularPosts(@Param("since") LocalDateTime since, Pageable pageable);

    // Posts by category IDs
    @Query("SELECT p FROM Post p " +
            "WHERE p.category.id IN :categoryIds " +
            "AND p.status = 'PUBLISHED' " +
            "ORDER BY p.createdAt DESC")
    List<Post> findByCategoryIds(@Param("categoryIds") Set<Long> categoryIds,
                                 @Param("limit") int limit);

    // Posts by tag IDs
    @Query("SELECT DISTINCT p FROM Post p " +
            "JOIN p.tags t " +
            "WHERE t.id IN :tagIds " +
            "AND p.status = 'PUBLISHED' " +
            "ORDER BY p.createdAt DESC")
    List<Post> findByTagIds(@Param("tagIds") Set<Long> tagIds,
                            @Param("limit") int limit);

    int countByCategoryAndStatus(Category category, PostStatus status);

    // Posts by author IDs
    @Query("SELECT p FROM Post p " +
            "WHERE p.author.id IN :authorIds " +
            "AND p.status = 'PUBLISHED' " +
            "ORDER BY p.createdAt DESC")
    Page<Post> findByAuthorIds(@Param("authorIds") Set<Long> authorIds,
                               Pageable pageable);
}