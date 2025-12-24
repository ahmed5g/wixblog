package com.tech.wixblog.repository;

import com.tech.wixblog.model.Post;
import com.tech.wixblog.model.User;
import com.tech.wixblog.model.UserInteraction;
import com.tech.wixblog.model.enums.InteractionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserInteractionRepository extends JpaRepository<UserInteraction, Long> {
    
    // Basic queries
    List<UserInteraction> findByUser (User user);
    List<UserInteraction> findByUserAndType (User user, InteractionType type);
    
    Optional<UserInteraction> findByUserAndPostAndType(User user, 
                                                      Post post,
                                                      InteractionType type);
    
    // Custom queries for recommendations
    @Query("SELECT DISTINCT ui.post.id FROM UserInteraction ui " +
           "WHERE ui.user = :user AND ui.type = :type " +
           "ORDER BY ui.interactedAt DESC")
    List<Long> findPostIdsByUserAndType(@Param("user") User user, 
                                       @Param("type") InteractionType type);
    
    @Query("SELECT ui FROM UserInteraction ui " +
           "WHERE ui.user = :user AND ui.interactedAt >= :since " +
           "ORDER BY ui.interactedAt DESC")
    List<UserInteraction> findByUserSince(@Param("user") User user, 
                                         @Param("since") LocalDateTime since);
}