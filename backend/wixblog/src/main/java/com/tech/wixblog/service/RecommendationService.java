package com.tech.wixblog.service;

import com.tech.wixblog.exception.ResourceNotFoundException;
import com.tech.wixblog.model.Post;
import com.tech.wixblog.model.Tag;
import com.tech.wixblog.model.User;
import com.tech.wixblog.model.UserInteraction;
import com.tech.wixblog.model.enums.InteractionType;
import com.tech.wixblog.repository.PostRepository;
import com.tech.wixblog.repository.UserInteractionRepository;
import com.tech.wixblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    
    private final UserInteractionRepository interactionRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FollowingService followingService;
    
    @Transactional(readOnly = true)
    public List<Post> getRecommendations (Long userId, int limit) {
        User user = getUser(userId);
        
        // 1. Get user's preferences
        Set<Long> likedPostIds = getLikedPostIds(user);
        Set<Long> preferredCategoryIds = getPreferredCategoryIds(user);
        Set<Long> preferredTagIds = getPreferredTagIds(user);
        
        // 2. If user has preferences, use content-based filtering
        if (!likedPostIds.isEmpty() || !preferredCategoryIds.isEmpty()) {
            return getContentBasedRecommendations(
                user, likedPostIds, preferredCategoryIds, preferredTagIds, limit);
        }
        
        // 3. Otherwise, use collaborative filtering with followed users
        return getCollaborativeRecommendations(user, limit);
    }
    
    private List<Post> getContentBasedRecommendations(User user, 
                                                     Set<Long> likedPostIds,
                                                     Set<Long> preferredCategoryIds,
                                                     Set<Long> preferredTagIds,
                                                     int limit) {
        
        List<Post> recommendations = new ArrayList<>();
        Map<Post, Integer> scores = new HashMap<>();
        
        // Similar to liked posts
        for (Long likedPostId : likedPostIds) {
            Post likedPost = postRepository.findById(likedPostId).orElse(null);
            if (likedPost == null) continue;
            
            // Find posts with similar tags
            Set<Long> tagIds = likedPost.getTags().stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());
            
            List<Post> similarPosts = postRepository.findByTagIds(tagIds, limit * 2);
            
            for (Post similarPost : similarPosts) {
                if (likedPostIds.contains(similarPost.getId())) continue;
                
                int similarity = calculateSimilarity(likedPost, similarPost);
                scores.merge(similarPost, similarity, Integer::sum);
            }
        }
        
        // Boost posts from preferred categories
        if (!preferredCategoryIds.isEmpty()) {
            List<Post> categoryPosts = postRepository.findByCategoryIds(preferredCategoryIds, limit);
            for (Post post : categoryPosts) {
                scores.merge(post, 10, Integer::sum);
            }
        }
        
        // Boost posts with preferred tags
        if (!preferredTagIds.isEmpty()) {
            List<Post> tagPosts = postRepository.findByTagIds(preferredTagIds, limit);
            for (Post post : tagPosts) {
                scores.merge(post, 5, Integer::sum);
            }
        }
        
        // Get top scored posts
        return scores.entrySet().stream()
            .sorted(Map.Entry.<Post, Integer>comparingByValue().reversed())
            .map(Map.Entry::getKey)
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    private List<Post> getCollaborativeRecommendations(User user, int limit) {
        // Get posts liked by followed users
        Set<Long> likedByFollowed = new HashSet<>();
        
        for (User followedUser : user.getFollowing()) {
            likedByFollowed.addAll(getLikedPostIds(followedUser));
        }
        
        // Remove posts already liked by user
        likedByFollowed.removeAll(getLikedPostIds(user));
        
        // Get the posts
        return likedByFollowed.stream()
            .map(postId -> postRepository.findById(postId).orElse(null))
            .filter(Objects::nonNull)
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    private int calculateSimilarity(Post post1, Post post2) {
        int score = 0;
        
        // Same category
        if (post1.getCategory().equals(post2.getCategory())) {
            score += 5;
        }
        
        // Common tags
        Set<Long> tags1 = post1.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        Set<Long> tags2 = post2.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        
        tags1.retainAll(tags2); // Intersection
        score += tags1.size() * 2;
        
        return score;
    }
    
    private Set<Long> getLikedPostIds(User user) {
        return interactionRepository.findByUserAndType(user, InteractionType.LIKE)
            .stream()
            .map(interaction -> interaction.getPost().getId())
            .collect(Collectors.toSet());
    }
    
    private Set<Long> getPreferredCategoryIds(User user) {
        // Get categories of liked posts
        Set<Long> categoryIds = new HashSet<>();
        
        interactionRepository.findByUserAndType(user, InteractionType.LIKE)
            .forEach(interaction -> {
                categoryIds.add(interaction.getPost().getCategory().getId());
            });
        
        return categoryIds;
    }
    
    private Set<Long> getPreferredTagIds(User user) {
        // Get tags of liked posts
        Set<Long> tagIds = new HashSet<>();
        
        interactionRepository.findByUserAndType(user, InteractionType.LIKE)
            .forEach(interaction -> {
                tagIds.addAll(interaction.getPost().getTags().stream()
                    .map(Tag::getId)
                    .collect(Collectors.toSet()));
            });
        
        return tagIds;
    }
    
    @Transactional
    public void recordInteraction(Long userId, Long postId, InteractionType type) {
        User user = getUser(userId);
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        // Check if interaction already exists
        Optional<UserInteraction> existing = interactionRepository
            .findByUserAndPostAndType(user, post, type);
        
        if (existing.isEmpty()) {
            UserInteraction interaction = UserInteraction.builder()
                .user(user)
                .post(post)
                .type(type)
                .interactedAt(LocalDateTime.now())
                .build();
            
            interactionRepository.save(interaction);
        }
    }
    
    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
    }
}