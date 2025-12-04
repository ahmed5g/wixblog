package com.tech.wixblog.repository;

import com.tech.wixblog.model.Post;
import com.tech.wixblog.model.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
}