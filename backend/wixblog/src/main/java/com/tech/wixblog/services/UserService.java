package com.tech.wixblog.services;

import com.tech.wixblog.models.Role;
import com.tech.wixblog.models.User;
import com.tech.wixblog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findOrCreateUser(String email, String name, String profilePicture) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // Update user info if changed
            if (!user.getName().equals(name) ||
                    (user.getProfilePicture() != null && !user.getProfilePicture().equals(profilePicture))) {
                user.setName(name);
                user.setProfilePicture(profilePicture);
                log.debug("Updated existing user: {}", email);
                return userRepository.save(user);
            }
            return user;
        } else {
            return createUser(email, name, profilePicture);
        }
    }

    @Transactional
    public User createUser(String email, String name, String profilePicture) {
        User user = User.builder()
                .email(email)
                .name(name)
                .profilePicture(profilePicture)
                .role(Role.ROLE_USER) // Auto-assign USER role
                .enabled(true)
                .build();

        log.info("Creating new user: {}", email);
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User updateUserRole(Long userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setRole(newRole);
        log.info("Updated user {} role to: {}", user.getEmail(), newRole);
        return userRepository.save(user);
    }

    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}