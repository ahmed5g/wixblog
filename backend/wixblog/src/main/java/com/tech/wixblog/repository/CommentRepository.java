package com.tech.wixblog.repository;

import com.tech.wixblog.model.Comment;
import com.tech.wixblog.model.enums.CommentStatus;
import com.tech.wixblog.model.Post;
import com.tech.wixblog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    // Basic queries
    Optional<Comment> findByIdAndStatus(Long id, CommentStatus status);

    // Find by post
    Page<Comment> findByPostAndStatusAndParentCommentIsNull(Post post, CommentStatus status, Pageable pageable);
    List<Comment> findByPostAndStatus(Post post, CommentStatus status);

    // Find by author
    Page<Comment> findByAuthorAndStatus(User author, CommentStatus status, Pageable pageable);

    // Find replies
    List<Comment> findByParentCommentAndStatusOrderByCreatedAtAsc(Comment parentComment, CommentStatus status);

    // Find with author eager loading
    @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.id = :id AND c.status = :status")
    Optional<Comment> findByIdWithAuthor(@Param("id") Long id, @Param("status") CommentStatus status);

    @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.post = :post AND c.status = :status AND c.parentComment IS NULL ORDER BY c.createdAt DESC")
    List<Comment> findTopLevelCommentsByPostWithAuthor(@Param("post") Post post, @Param("status") CommentStatus status);

    // Statistics
    Long countByPostAndStatus(Post post, CommentStatus status);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.author = :author")
    Long countTotalCommentsByAuthor(@Param("author") User author);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.author.id = :userId")
    Long countTotalCommentsByAuthorId(@Param("userId") Long userId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId AND c.status = :status")
    Long countByPostIdAndStatus(@Param("postId") Long postId, @Param("status") CommentStatus status);

    // Find replies count
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.parentComment.id = :parentCommentId AND c.status = :status")
    Long countRepliesByParentCommentId(@Param("parentCommentId") Long parentCommentId, @Param("status") CommentStatus status);
}