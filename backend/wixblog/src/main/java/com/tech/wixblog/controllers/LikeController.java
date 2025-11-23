package com.tech.wixblog.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
@RequiredArgsConstructor
public class LikeController {
    
//    private final LikeService likeService;
//    private final UserService userService;
//
//    @PostMapping
//    public ResponseEntity<LikeDTO> likePost (
//            @PathVariable Long postId,
//            @AuthenticationPrincipal OAuth2User oAuth2User) {
//        User currentUser=userService.extractUserFromOAuth2User(oAuth2User);
//        Optional<LikeDTO> like = likeService.likePost(postId, currentUser);
//        return like.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.badRequest().build());
//    }
//
//    @DeleteMapping
//    public ResponseEntity<Void> unlikePost(
//            @PathVariable Long postId,
//            @AuthenticationPrincipal OAuth2User oAuth2User) {
//        User currentUser = userService.extractUserFromOAuth2User(oAuth2User);
//        boolean unliked = likeService.unlikePost(postId, currentUser);
//        return unliked ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
//    }
//
//    @GetMapping("/count")
//    public ResponseEntity<Long> getLikeCount(@PathVariable Long postId) {
//        Long count = likeService.getPostLikeCount(postId);
//        return ResponseEntity.ok(count);
//    }
//
//    @GetMapping("/has-liked")
//
//    public ResponseEntity<Boolean> hasLikedPost(
//            @PathVariable Long postId,
//            @AuthenticationPrincipal OAuth2User oAuth2User) {
//        User currentUser=userService.extractUserFromOAuth2User(oAuth2User);
//        boolean hasLiked = likeService.isPostLikedByUser(postId, currentUser);
//        return ResponseEntity.ok(hasLiked);
//    }
//
//    @GetMapping("/users")
//    public ResponseEntity<List<UserDTO>> getUsersWhoLikedPost (
//            @PathVariable Long postId,
//            @ParameterObject Pageable pageable) {
//        Page<UserDTO> users = likeService.getUsersWhoLikedPost(postId, pageable);
//        return ResponseEntity.ok(users.getContent());
//    }
}