package com.tech.wixblog.controllers;

import com.tech.wixblog.services.PostViewService;
import com.tech.wixblog.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/views")
@RequiredArgsConstructor
public class PostViewController {
    private final PostViewService postViewService;
    private final UserService userService;
//
//    @PostMapping
//    public ResponseEntity<PostViewDTO> trackView (
//            @PathVariable Long postId,
//            @AuthenticationPrincipal OAuth2User currentUser,
//            HttpServletRequest request) {
//        User user = userService.extractUserFromOAuth2User(currentUser);
//        Optional<PostViewDTO> view = postViewService.trackPostView(postId, user, request);
//        return view.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.badRequest().build());
//    }
//
//    //POST VIEWS COUNTS
//    @GetMapping("/count")
//    public ResponseEntity<Long> getViewCount (@PathVariable Long postId) {
//        Long count = postViewService.getPostViewCount(postId);
//        return ResponseEntity.ok(count);
//    }
//
//    //View post confirmation
//    @GetMapping("/has-viewed")
//    public ResponseEntity<Boolean> hasViewedPost (
//            @PathVariable Long postId,
//            @AuthenticationPrincipal OAuth2User currentUser) {
//        User user = userService.extractUserFromOAuth2User(currentUser);
//        boolean hasViewed = postViewService.isPostViewedByUser(postId, user);
//        return ResponseEntity.ok(hasViewed);
//    }
//
//    //POST VIEW STATS
//    @GetMapping
//    public ResponseEntity<Page<PostViewDTO>> getPostViews (
//            @PathVariable Long postId,
//            @ParameterObject Pageable pageable) {
//        Page<PostViewDTO> views = postViewService.getPostViewsStats(postId, pageable);
//        return ResponseEntity.ok(views);
//    }
}