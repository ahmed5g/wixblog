package com.tech.wixblog.repository;

import com.tech.wixblog.model.User;
import com.tech.wixblog.model.enums.AuthProvider;
import com.tech.wixblog.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail (String email);

    boolean existsByEmail(String email);

    List<User> findByEnabledTrue ();

    List<User> findByRole (Role role);

    List<User> findByEnabled(Boolean enabled);

    @Query("SELECT u FROM User u WHERE u.name LIKE %:name% OR u.email LIKE %:email%")
    List<User> findByNameOrEmailContaining (@Param("name") String name, @Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.lastLoginAt >= :since")
    List<User> findActiveUsersSince (@Param("since") LocalDateTime since);

    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    List<User> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(u) FROM User u WHERE u.enabled = true")
    long countActiveUsers();

    @Query("SELECT u.role, COUNT(u) FROM User u GROUP BY u.role")
    Map<Role, Long> countUsersByRole ();

    @Query("SELECT u FROM User u JOIN u.oauthProviders p WHERE KEY(p) = :provider AND p.providerUserId = :providerUserId")
    Optional<User> findByOAuthProvider(@Param("provider") AuthProvider provider,
                                       @Param("providerUserId") String providerUserId);




    // FIXED: Users followed by people you follow
    @Query("SELECT DISTINCT f2 FROM User u " +
            "JOIN u.following f1 " +          // People you follow
            "JOIN f1.following f2 " +          // People they follow
            "WHERE u.id = :userId " +
            "AND f2.id != :userId " +          // Exclude yourself
            "AND f2 NOT IN (SELECT f FROM User u2 JOIN u2.following f WHERE u2.id = :userId)") // Exclude already followed
    List<User> findUsersFollowedByFollowedUsers (@Param("userId") Long userId, Pageable pageable);

    // Alternative simpler version
    @Query("SELECT u FROM User u " +
            "WHERE u.id IN " +
            "(SELECT f2.id FROM User u1 " +
            " JOIN u1.following f1 " +
            " JOIN f1.following f2 " +
            " WHERE u1.id = :userId) " +
            "AND u.id != :userId " +
            "AND u.id NOT IN " +
            "(SELECT f.id FROM User u2 JOIN u2.following f WHERE u2.id = :userId)")
    List<User> findSuggestedUsers(@Param("userId") Long userId, Pageable pageable);

    // Even simpler: Get users with mutual connections
    @Query("SELECT u FROM User u " +
            "WHERE u.id IN " +
            "(SELECT f2.id FROM User u1 " +
            " JOIN u1.following f1 " +
            " JOIN f1.following f2 " +
            " WHERE u1.id = :userId " +
            " GROUP BY f2.id " +
            " HAVING COUNT(f1.id) >= 2)") // At least 2 mutual connections
    List<User> findUsersWithMutualConnections(@Param("userId") Long userId, Pageable pageable);
}