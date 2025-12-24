package com.tech.wixblog.service;

import com.tech.wixblog.exception.ResourceNotFoundException;
import com.tech.wixblog.model.*;
import com.tech.wixblog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowingService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    // ========== USER FOLLOWING ==========

    @Transactional
    public void followUser(Long followerId, Long followingId) {
        User follower = getUser(followerId);
        User following = getUser(followingId);
        follower.followUser(following);
        userRepository.save(follower);
    }

    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        User follower = getUser(followerId);
        User following = getUser(followingId);
        follower.unfollowUser(following);
        userRepository.save(follower);
    }

    @Transactional(readOnly = true)
    public boolean isFollowingUser(Long followerId, Long followingId) {
        User follower = getUser(followerId);
        User following = getUser(followingId);
        return follower.isFollowingUser(following);
    }

    // ========== CATEGORY FOLLOWING ==========

    @Transactional
    public void followCategory(Long userId, String categorySlug) {
        User user = getUser(userId);
        Category category = getCategory(categorySlug);
        user.followCategory(category);
        userRepository.save(user);
    }

    @Transactional
    public void unfollowCategory(Long userId, String categorySlug) {
        User user = getUser(userId);
        Category category = getCategory(categorySlug);
        user.unfollowCategory(category);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean isFollowingCategory(Long userId, String categorySlug) {
        User user = getUser(userId);
        Category category = getCategory(categorySlug);
        return user.isFollowingCategory(category);
    }

    // ========== TAG FOLLOWING ==========

    @Transactional
    public void followTag(Long userId, String tagSlug) {
        User user = getUser(userId);
        Tag tag = getTag(tagSlug);
        user.followTag(tag);
        userRepository.save(user);
    }

    @Transactional
    public void unfollowTag(Long userId, String tagSlug) {
        User user = getUser(userId);
        Tag tag = getTag(tagSlug);
        user.unfollowTag(tag);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean isFollowingTag(Long userId, String tagSlug) {
        User user = getUser(userId);
        Tag tag = getTag(tagSlug);
        return user.isFollowingTag(tag);
    }

    // ========== FEED GENERATION ==========

    @Transactional(readOnly = true)
    public Page<Post> getUserFeed(Long userId, Pageable pageable) {
        User user = getUser(userId);

        Set<Long> followedUserIds = extractIds(user.getFollowing());
        Set<Long> followedCategoryIds = extractIds(user.getFollowedCategories());
        Set<Long> followedTagIds = extractIds(user.getFollowedTags());

        if (followedUserIds.isEmpty() && followedCategoryIds.isEmpty() && followedTagIds.isEmpty()) {
            return getPopularPosts(pageable);
        }

        return postRepository.findPostsForFeed(followedUserIds, followedCategoryIds, followedTagIds, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Post> getPopularPosts(Pageable pageable) {
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        return postRepository.findPopularPosts(weekAgo, pageable);
    }

    // ========== STATISTICS ==========

    @Transactional(readOnly = true)
    public Map<String, Integer> getFollowStats(Long userId) {
        User user = getUser(userId);

        Map<String, Integer> stats = new HashMap<>();
        stats.put("following", user.getFollowing().size());
        stats.put("followers", user.getFollowers().size());
        stats.put("followedCategories", user.getFollowedCategories().size());
        stats.put("followedTags", user.getFollowedTags().size());

        return stats;
    }

    // ========== SUGGESTIONS ==========

    @Transactional(readOnly = true)
    public List<User> getSuggestedUsers(Long userId, int limit) {
        User user = getUser(userId);

        Set<User> suggestions = new HashSet<>();
        for (User followedUser : user.getFollowing()) {
            suggestions.addAll(followedUser.getFollowing());
        }

        suggestions.remove(user);
        suggestions.removeAll(user.getFollowing());

        return suggestions.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Category> getSuggestedCategories(Long userId, int limit) {
        User user = getUser(userId);
        Set<Category> followed = user.getFollowedCategories();

        return categoryRepository.findAll().stream()
                .filter(category -> !followed.contains(category))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Tag> getSuggestedTags(Long userId, int limit) {
        User user = getUser(userId);
        Set<Tag> followed = user.getFollowedTags();

        return tagRepository.findAll().stream()
                .filter(tag -> !followed.contains(tag))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // ========== HELPER METHODS ==========

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
    }

    private Category getCategory(String slug) {
        return categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + slug));
    }

    private Tag getTag(String slug) {
        return tagRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found: " + slug));
    }

    private <T> Set<Long> extractIds(Set<T> entities) {
        return entities.stream()
                .map(entity -> {
                    if (entity instanceof User) return ((User) entity).getId();
                    if (entity instanceof Category) return ((Category) entity).getId();
                    if (entity instanceof Tag) return ((Tag) entity).getId();
                    throw new IllegalArgumentException("Unsupported entity type");
                })
                .collect(Collectors.toSet());
    }
}