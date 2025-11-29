package com.tech.wixblog.repositories;

import com.tech.wixblog.model.Role;
import com.tech.wixblog.model.User;
import com.tech.wixblog.model.enums.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}