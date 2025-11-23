package com.tech.wixblog.repositories;

import com.tech.wixblog.model.Comment;
import com.tech.wixblog.model.Post;
import com.tech.wixblog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    Page<Comment> findByPostAndParentCommentIsNullOrderByCreatedAtDesc(Post post, Pageable pageable);
    
    List<Comment> findByParentCommentOrderByCreatedAtAsc(Comment parentComment);
    
    Page<Comment> findByAuthorOrderByCreatedAtDesc(User author, Pageable pageable);
    
    Long countByPost(Post post);

    List<Comment> findByPost(Post post);
    
    @Query("SELECT c FROM Comment c WHERE c.isApproved = false")
    Page<Comment> findUnapprovedComments(Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post = :post AND c.isApproved = true")
    Long countApprovedCommentsByPost(@Param("post") Post post);
}