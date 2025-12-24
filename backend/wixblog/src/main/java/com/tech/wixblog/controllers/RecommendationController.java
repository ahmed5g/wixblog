package com.tech.wixblog.controllers;

import com.tech.wixblog.model.Post;
import com.tech.wixblog.model.enums.InteractionType;
import com.tech.wixblog.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;


    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Post>> getRecommendations (
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        List<Post> recommendations = recommendationService.getRecommendations(userId, limit);
        return ResponseEntity.ok(recommendations);
    }

    @PostMapping("/interactions")
    public ResponseEntity<String> recordInteraction (
            @RequestParam Long userId,
            @RequestParam Long postId,
            @RequestParam String type) {
        InteractionType interactionType =
                InteractionType.valueOf(type.toUpperCase());
        recommendationService.recordInteraction(userId, postId, interactionType);
        return ResponseEntity.ok("Interaction recorded");
    }
}