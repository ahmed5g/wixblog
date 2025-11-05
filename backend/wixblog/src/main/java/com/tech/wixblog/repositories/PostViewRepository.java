package com.tech.wixblog.repositories;

import com.tech.wixblog.models.Post;
import com.tech.wixblog.models.PostView;
import com.tech.wixblog.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostViewRepository extends JpaRepository<PostView, Long> {
    
    Long countByPost(Post post);
    
    Boolean existsByPostAndUser(Post post, User user);
    
    @Query("SELECT pv FROM PostView pv WHERE pv.post = :post ORDER BY pv.viewedAt DESC")
    Page<PostView> findByPost(@Param("post") Post post, Pageable pageable);
    
    @Query("SELECT COUNT(pv) FROM PostView pv WHERE pv.post.author = :author")
    Long countViewsOnAuthorPosts(@Param("author") User author);
    
    @Query("SELECT pv FROM PostView pv WHERE pv.viewedAt >= :startDate AND pv.viewedAt <= :endDate")
    List<PostView> findViewsBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);
}