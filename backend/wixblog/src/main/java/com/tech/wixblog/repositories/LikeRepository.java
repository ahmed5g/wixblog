package com.tech.wixblog.repositories;

import com.tech.wixblog.models.Like;
import com.tech.wixblog.models.Post;
import com.tech.wixblog.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    Optional<Like> findByPostAndUser(Post post, User user);
    
    Boolean existsByPostAndUser(Post post, User user);
    
    Long countByPost(Post post);
    
    @Query("SELECT COUNT(l) FROM Like l WHERE l.post.author = :user")
    Long countLikesOnUserPosts(@Param("user") User user);

    // Find users who liked a specific post
    @Query("SELECT l.user FROM Like l WHERE l.post = :post")
    Page<User> findUsersByPost(@Param("post") Post post, Pageable pageable);

    Page<Like> findByPost (Post post, Pageable pageable);
}