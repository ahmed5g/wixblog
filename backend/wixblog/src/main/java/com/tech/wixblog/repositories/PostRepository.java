package com.tech.wixblog.repositories;

import com.tech.wixblog.model.Post;
import com.tech.wixblog.model.PostStatus;
import com.tech.wixblog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    // Find published posts with pagination
    Page<Post> findByStatusOrderByCreatedAtDesc(PostStatus status, Pageable pageable);
    
    // Find posts by author
    Page<Post> findByAuthorIdOrderByCreatedAtDesc(Long authorId, Pageable pageable);
    // Find all posts by author (for dashboard)
    Page<Post> findByAuthorOrderByCreatedAtDesc(User author, Pageable pageable);
    
    // Find featured posts
    List<Post> findByIsFeaturedTrueAndStatusOrderByCreatedAtDesc(PostStatus status, Pageable pageable);
    
    // Find by slug
    Optional<Post> findBySlugAndStatus(String slug, PostStatus status);
    
    // Search posts
    @Query("SELECT p FROM Post p WHERE " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.content) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.summary) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "p.status = :status")
    Page<Post> searchPosts(@Param("query") String query, @Param("status") PostStatus status, Pageable pageable);



    // Count posts by status
    Long countByStatus(PostStatus status);
    
    // Find most viewed posts
    @Query("SELECT p FROM Post p WHERE p.status = :status ORDER BY p.viewCount DESC")
    List<Post> findTopByViewCount(@Param("status") PostStatus status, Pageable pageable);
    
    // Update view count
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    void incrementViewCount(@Param("postId") Long postId);

    Page<Post> findByAuthorAndStatusOrderByCreatedAtDesc (User author, PostStatus postStatus, Pageable pageable);
}