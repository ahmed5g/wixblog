package com.tech.wixblog.controllers;

import com.tech.wixblog.model.Category;
import com.tech.wixblog.model.Post;
import com.tech.wixblog.model.Tag;
import com.tech.wixblog.model.User;
import com.tech.wixblog.service.FollowingService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowingController {
    private final FollowingService followingService;

    // ========== USER FOLLOWING ==========

    @PostMapping("/users/{followerId}/follow/{followingId}")
    public ResponseEntity<Void> followUser(
            @PathVariable Long followerId,
            @PathVariable Long followingId) {

        if (followerId.equals(followingId)) {
            return ResponseEntity.badRequest().build();
        }

        followingService.followUser(followerId, followingId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{followerId}/follow/{followingId}")
    public ResponseEntity<Void> unfollowUser(
            @PathVariable Long followerId,
            @PathVariable Long followingId) {

        followingService.unfollowUser(followerId, followingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{followerId}/is-following/{followingId}")
    public ResponseEntity<Boolean> isFollowingUser(
            @PathVariable Long followerId,
            @PathVariable Long followingId) {

        boolean isFollowing = followingService.isFollowingUser(followerId, followingId);
        return ResponseEntity.ok(isFollowing);
    }

    // ========== CATEGORY FOLLOWING ==========

    @PostMapping("/users/{userId}/categories/{categorySlug}/follow")
    public ResponseEntity<Void> followCategory(
            @PathVariable Long userId,
            @PathVariable String categorySlug) {

        followingService.followCategory(userId, categorySlug);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{userId}/categories/{categorySlug}/follow")
    public ResponseEntity<Void> unfollowCategory(
            @PathVariable Long userId,
            @PathVariable String categorySlug) {

        followingService.unfollowCategory(userId, categorySlug);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}/categories/{categorySlug}/is-following")
    public ResponseEntity<Boolean> isFollowingCategory(
            @PathVariable Long userId,
            @PathVariable String categorySlug) {

        boolean isFollowing = followingService.isFollowingCategory(userId, categorySlug);
        return ResponseEntity.ok(isFollowing);
    }

    // ========== TAG FOLLOWING ==========

    @PostMapping("/users/{userId}/tags/{tagSlug}/follow")
    public ResponseEntity<Void> followTag(
            @PathVariable Long userId,
            @PathVariable String tagSlug) {

        followingService.followTag(userId, tagSlug);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{userId}/tags/{tagSlug}/follow")
    public ResponseEntity<Void> unfollowTag(
            @PathVariable Long userId,
            @PathVariable String tagSlug) {

        followingService.unfollowTag(userId, tagSlug);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}/tags/{tagSlug}/is-following")
    public ResponseEntity<Boolean> isFollowingTag(
            @PathVariable Long userId,
            @PathVariable String tagSlug) {

        boolean isFollowing = followingService.isFollowingTag(userId, tagSlug);
        return ResponseEntity.ok(isFollowing);
    }

    // ========== FEEDS ==========

    @GetMapping("/users/{userId}/feed")
    public ResponseEntity<Page<Post>> getUserFeed(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Post> feed = followingService.getUserFeed(userId, PageRequest.of(page, size));
        return ResponseEntity.ok(feed);
    }

    @GetMapping("/feed/popular")
    public ResponseEntity<Page<Post>> getPopularFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Post> popular = followingService.getPopularPosts(PageRequest.of(page, size));
        return ResponseEntity.ok(popular);
    }

    // ========== STATISTICS ==========

    @GetMapping("/users/{userId}/stats")
    public ResponseEntity<Map<String, Integer>> getFollowStats(@PathVariable Long userId) {
        Map<String, Integer> stats = followingService.getFollowStats(userId);
        return ResponseEntity.ok(stats);
    }

    // ========== SUGGESTIONS ==========

    @GetMapping("/users/{userId}/suggestions/users")
    public ResponseEntity<List<User>> getSuggestedUsers(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") @Min(1) int limit) {

        List<User> suggestions = followingService.getSuggestedUsers(userId, limit);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/users/{userId}/suggestions/categories")
    public ResponseEntity<List<Category>> getSuggestedCategories(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") @Min(1) int limit) {

        List<Category> suggestions = followingService.getSuggestedCategories(userId, limit);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/users/{userId}/suggestions/tags")
    public ResponseEntity<List<Tag>> getSuggestedTags(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") @Min(1) int limit) {

        List<Tag> suggestions = followingService.getSuggestedTags(userId, limit);
        return ResponseEntity.ok(suggestions);
    }
}