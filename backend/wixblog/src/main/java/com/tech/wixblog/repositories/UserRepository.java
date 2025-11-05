package com.tech.wixblog.repositories;

import com.tech.wixblog.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);



    // Search users by name, email, or bio
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.bio) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<User> searchUsers(@Param("query") String query, Pageable pageable);

    // Find top writers by published posts
    @Query("SELECT u FROM User u WHERE u.publishedPosts > 0 ORDER BY u.publishedPosts DESC, u.totalLikesReceived DESC")
    Page<User> findTopWriters(Pageable pageable);

    // Find active users (recent activity)
    @Query("SELECT u FROM User u WHERE u.lastActivityAt IS NOT NULL ORDER BY u.lastActivityAt DESC")
    Page<User> findActiveUsers(Pageable pageable);

    // Count users by role (for admin dashboard)
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    Long countByRole(@Param("role") String role);
}
