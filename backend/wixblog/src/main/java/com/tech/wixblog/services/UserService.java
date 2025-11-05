package com.tech.wixblog.services;

import com.tech.wixblog.dto.UpdateProfileDTO;
import com.tech.wixblog.dto.UpdateProfileStatsDTO;
import com.tech.wixblog.dto.UpdateSettingsDTO;
import com.tech.wixblog.dto.UserDTO;
import com.tech.wixblog.mapper.UserMapper;
import com.tech.wixblog.models.Role;
import com.tech.wixblog.models.User;
import com.tech.wixblog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public OAuth2User loadUser (OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException("Email not found in OAuth2 provider response");
        }
        String name = (String) attributes.getOrDefault("name", "Anonymous User");
        String picture = (String) attributes.getOrDefault("picture", null);
        User user = findOrCreateUser(email, name, picture);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
        return new DefaultOAuth2User(
                Collections.singleton(authority),
                attributes,
                "email"
        );
    }

    public User extractUserFromOAuth2User (OAuth2User principal) {
        if (principal == null) {
            throw new IllegalArgumentException("No user is connected at the moment");
        }
        String email = principal.getAttribute("email");
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("OAuth2User does not have a valid email attribute");
        }
        String tempName = principal.getAttribute("name");
        final String name = (tempName == null || tempName.isBlank()) ? "Anonymous User" : tempName;
        final String picture = principal.getAttribute("picture");
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .name(name)
                            .profilePicture(picture)
                            .role(Role.ROLE_USER)
                            .enabled(true)
                            .joinDate(java.time.LocalDateTime.now())
                            .lastLoginAt(java.time.LocalDateTime.now())
                            .build();
                    return userRepository.save(newUser);
                });
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserProfile (Long userId, User currentUser) {
        return userRepository.findById(userId)
                .map(user -> {
                    UserDTO userDTO = userMapper.userToUserDTO(user);
                    // Hide sensitive information if not the profile owner or admin
                    if (currentUser == null || (!user.getId().equals(
                            currentUser.getId()) && !currentUser.isAdmin())) {
                        userDTO.setEmail(null);
                        userDTO.setEmailNotifications(null);
                        userDTO.setCommentNotifications(null);
                        userDTO.setLikeNotifications(null);
                        userDTO.setNewsletterSubscribed(null);
                        userDTO.setShowOnlineStatus(null);
                    }
                    return userDTO;
                });
    }

    @Transactional
    public Optional<UserDTO> updateUserProfile (Long userId,
                                                UpdateProfileDTO updateProfileDTO,
                                                User currentUser) {
        return userRepository.findById(userId)
                .map(user -> {
                    // Check if user owns the profile or is admin
                    if (!user.getId().equals(currentUser.getId()) && !currentUser.isAdmin()) {
                        throw new AccessDeniedException("You can only update your own profile");
                    }
                    // Update profile fields
                    if (updateProfileDTO.getName() != null) {
                        user.setName(updateProfileDTO.getName());
                    }
                    if (updateProfileDTO.getBio() != null) {
                        user.setBio(updateProfileDTO.getBio());
                    }
                    if (updateProfileDTO.getWebsiteUrl() != null) {
                        user.setWebsiteUrl(updateProfileDTO.getWebsiteUrl());
                    }
                    if (updateProfileDTO.getTwitterHandle() != null) {
                        user.setTwitterHandle(updateProfileDTO.getTwitterHandle());
                    }
                    if (updateProfileDTO.getGithubUsername() != null) {
                        user.setGithubUsername(updateProfileDTO.getGithubUsername());
                    }
                    if (updateProfileDTO.getLinkedinUrl() != null) {
                        user.setLinkedinUrl(updateProfileDTO.getLinkedinUrl());
                    }
                    if (updateProfileDTO.getLocation() != null) {
                        user.setLocation(updateProfileDTO.getLocation());
                    }
                    if (updateProfileDTO.getCompany() != null) {
                        user.setCompany(updateProfileDTO.getCompany());
                    }
                    if (updateProfileDTO.getJobTitle() != null) {
                        user.setJobTitle(updateProfileDTO.getJobTitle());
                    }
                    User updatedUser = userRepository.save(user);
                    return userMapper.userToUserDTO(updatedUser);
                });
    }

    @Transactional
    public Optional<UserDTO> updateUserSettings (Long userId,
                                                 UpdateSettingsDTO updateSettingsDTO,
                                                 User currentUser) {
        return userRepository.findById(userId)
                .map(user -> {
                    // Check if user owns the profile
                    if (!user.getId().equals(currentUser.getId())) {
                        throw new AccessDeniedException("You can only update your own settings");
                    }
                    // Update settings fields
                    if (updateSettingsDTO.getEmailNotifications() != null) {
                        user.setEmailNotifications(updateSettingsDTO.getEmailNotifications());
                    }
                    if (updateSettingsDTO.getCommentNotifications() != null) {
                        user.setCommentNotifications(updateSettingsDTO.getCommentNotifications());
                    }
                    if (updateSettingsDTO.getLikeNotifications() != null) {
                        user.setLikeNotifications(updateSettingsDTO.getLikeNotifications());
                    }
                    if (updateSettingsDTO.getNewsletterSubscribed() != null) {
                        user.setNewsletterSubscribed(updateSettingsDTO.getNewsletterSubscribed());
                    }
                    if (updateSettingsDTO.getPublicProfile() != null) {
                        user.setPublicProfile(updateSettingsDTO.getPublicProfile());
                    }
                    if (updateSettingsDTO.getShowOnlineStatus() != null) {
                        user.setShowOnlineStatus(updateSettingsDTO.getShowOnlineStatus());
                    }
                    User updatedUser = userRepository.save(user);
                    return userMapper.userToUserDTO(updatedUser);
                });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> searchUsers (String query, Pageable pageable) {
        Page<User> users = userRepository.searchUsers(query, pageable);
        return users.map(userMapper::userToUserDTO);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getTopWriters (Pageable pageable) {
        Page<User> users = userRepository.findTopWriters(pageable);
        return users.map(userMapper::userToUserDTO);
    }

    @Transactional
    public void updateUserStats (User user, UpdateProfileStatsDTO profileStats) {
        if (user == null) return;
        if (profileStats.getPostsDelta() != null)
            user.setTotalPosts(
                    (user.getTotalPosts() != null ? user.getTotalPosts() : 0) + profileStats.getPostsDelta());
        if (profileStats.getPublishedPostsDelta() != null)
            user.setPublishedPosts(
                    (user.getPublishedPosts() != null ? user.getPublishedPosts() : 0) + profileStats.getPublishedPostsDelta());
        if (profileStats.getLikesDelta() != null)
            user.setTotalLikesReceived(
                    (user.getTotalLikesReceived() != null ? user.getTotalLikesReceived() : 0) + profileStats.getLikesDelta());
        if (profileStats.getCommentsDelta() != null)
            user.setTotalCommentsReceived(
                    (user.getTotalCommentsReceived() != null ? user.getTotalCommentsReceived() : 0) + profileStats.getCommentsDelta());
        if (profileStats.getViewsDelta() != null)
            user.setTotalViewsReceived(
                    (user.getTotalViewsReceived() != null ? user.getTotalViewsReceived() : 0) + profileStats.getViewsDelta());
        user.setLastActivityAt(java.time.LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void recordUserLogin (String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    user.recordLogin();
                    userRepository.save(user);
                });
    }


    /*
     * ADMIN PANEL METHODS
     * */

    @Transactional
    public UserDTO updateUserRole (User currentUser, Role newRole) {
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new RuntimeException(
                        "User not found with unique id(Email): " + currentUser.getEmail()));
        user.setRole(newRole);
        userRepository.save(user);
        return userMapper.userToUserDTO(user);
    }

    public Optional<User> getUserById (Long userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    public User findOrCreateUser (String email, String name, String profilePicture) {
        Optional<User> existingUserOpt = userRepository.findByEmail(email);
        if (existingUserOpt.isPresent()) {
            User user = existingUserOpt.get();
            boolean updated = false;
            // Update name if changed
            if (!name.equals(user.getName())) {
                user.setName(name);
                updated = true;
            }
            // Update profile picture if changed
            if (profilePicture != null && !profilePicture.equals(user.getProfilePicture())) {
                user.setProfilePicture(profilePicture);
                updated = true;
            }
            if (updated) {
                user.setLastLoginAt(LocalDateTime.now());
                userRepository.save(user);
            }
            return user;
        } else {
            // Create new user
            User newUser = User.builder()
                    .email(email)
                    .name(name)
                    .profilePicture(profilePicture)
                    .role(Role.ROLE_USER)
                    .enabled(true)
                    .joinDate(LocalDateTime.now())
                    .lastLoginAt(LocalDateTime.now())
                    .build();
            return userRepository.save(newUser);
        }
    }

    public List<UserDTO> findAllUsers () {
        List<User> users = userRepository.findAll();
        return userMapper.userToUserDTOs(users);

    }


}