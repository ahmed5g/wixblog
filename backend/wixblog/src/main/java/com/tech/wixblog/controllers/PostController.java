package com.tech.wixblog.controllers;

import com.tech.wixblog.services.PostService;
import com.tech.wixblog.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;

//    @GetMapping
//    public ResponseEntity<Page<PostDTO>> getPublishedPosts (@ParameterObject Pageable pageable,
//                                                            @AuthenticationPrincipal
//                                                            OAuth2User principal) {
//        User currentUser = userService.extractUserFromOAuth2User(principal);
//        Page<PostDTO> posts = postService.getPublishedPosts(pageable, currentUser);
//        return ResponseEntity.ok(posts);
//    }
//
//    @GetMapping("/{slug}")
//    public ResponseEntity<PostDTO> getPostBySlug (@PathVariable String slug,
//                                                  @AuthenticationPrincipal OAuth2User principal) {
//        User currentUser = userService.extractUserFromOAuth2User(principal);
//        Optional<PostDTO> post = postService.getPublishedPostBySlug(slug, currentUser);
//        return post.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//    }
//
//
//
//    @PostMapping("/")
//    public ResponseEntity<List<PostDTO>> createPosts(@Valid @RequestBody List<CreatePostDTO> createPostDTOs,
//                                                     @AuthenticationPrincipal OAuth2User principal) {
//        User author = userService.extractUserFromOAuth2User(principal);
//        List<PostDTO> createdPosts = postService.createPosts(createPostDTOs, author);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdPosts);
//    }
//
//
//    @PatchMapping("/bulk")
//    public ResponseEntity<List<PostDTO>> updatePosts(
//            @Valid @RequestBody List<UpdatePostDTO> updatePostDTOs,
//            @AuthenticationPrincipal OAuth2User principal) {
//
//        User currentUser = userService.extractUserFromOAuth2User(principal);
//        List<PostDTO> updatedPosts = postService.updatePosts(updatePostDTOs, currentUser);
//        return ResponseEntity.ok(updatedPosts);
//    }
//
//    // Keep your existing single update endpoint
//    @PatchMapping("/{postId}")
//    public ResponseEntity<PostDTO> updatePost(
//            @PathVariable Long postId,
//            @Valid @RequestBody UpdatePostDTO updatePostDTO,
//            @AuthenticationPrincipal OAuth2User principal) {
//
//        User currentUser = userService.extractUserFromOAuth2User(principal);
//        Optional<PostDTO> updatedPost = postService.updatePost(postId, updatePostDTO, currentUser);
//        return updatedPost.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//    }
//    @DeleteMapping("/{postId}")
//
//    public ResponseEntity<Void> deletePost (@PathVariable Long postId,
//                                            @AuthenticationPrincipal OAuth2User principal) {
//        User currentUser = userService.extractUserFromOAuth2User(principal);
//        boolean deleted = postService.deletePost(postId, currentUser);
//        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<Page<PostDTO>> searchPosts (@RequestParam String query,
//                                                      @ParameterObject Pageable pageable,
//                                                      @AuthenticationPrincipal
//                                                      OAuth2User principal) {
//        User currentUser = userService.extractUserFromOAuth2User(principal);
//        Page<PostDTO> posts = postService.searchPosts(query, pageable, currentUser);
//        return ResponseEntity.ok(posts);
//    }
//
//    @GetMapping("/featured")
//    public ResponseEntity<Page<PostDTO>> getFeaturedPosts (@ParameterObject Pageable pageable,
//                                                           @AuthenticationPrincipal
//                                                           OAuth2User principal) {
//        User currentUser = userService.extractUserFromOAuth2User(principal);
//        Page<PostDTO> posts = postService.getFeaturedPosts(pageable, currentUser);
//        return ResponseEntity.ok(posts);
//    }
//
//    @GetMapping("/my-posts")
//
//    public ResponseEntity<List<PostDTO>> getMyPosts (@ParameterObject Pageable pageable,
//                                                     @AuthenticationPrincipal
//                                                     OAuth2User principal) {
//        User currentUser = userService.extractUserFromOAuth2User(principal);
//        Page<PostDTO> posts = postService.getUserPosts(currentUser, pageable, currentUser);
//        return ResponseEntity.ok(posts.getContent());
//    }
//
//
//    @PostMapping("/{postId}/publish")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<PostDTO> publishPost(@PathVariable Long postId,
//                                               @AuthenticationPrincipal OAuth2User principal) {
//        User currentUser = userService.extractUserFromOAuth2User(principal);
//        Optional<PostDTO> publishedPost = postService.publishPost(postId, currentUser);
//        return publishedPost.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//    }
//
//    // Optional: Add unpublish endpoint
//    @PostMapping("/{postId}/unpublish")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<PostDTO> unpublishPost(@PathVariable Long postId,
//                                                 @AuthenticationPrincipal OAuth2User principal) {
//        User currentUser = userService.extractUserFromOAuth2User(principal);
//        Optional<PostDTO> unpublishedPost = postService.unpublishPost(postId, currentUser);
//        return unpublishedPost.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//    }
//
//
//    // In PostController - get user's drafts
//    @GetMapping("/my-drafts")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<Page<PostDTO>> getMyDrafts(@ParameterObject Pageable pageable,
//                                                     @AuthenticationPrincipal OAuth2User principal) {
//        User currentUser = userService.extractUserFromOAuth2User(principal);
//        Page<PostDTO> drafts = postService.getUserDrafts(currentUser, pageable, currentUser);
//        return ResponseEntity.ok(drafts);
//    }
//
//    // Get all posts by status (for admin or post management)
//    @GetMapping("/status/{status}")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<Page<PostDTO>> getPostsByStatus(@PathVariable PostStatus status,
//                                                          @ParameterObject Pageable pageable,
//                                                          @AuthenticationPrincipal OAuth2User principal) {
//        User currentUser = userService.extractUserFromOAuth2User(principal);
//        Page<PostDTO> posts = postService.getPostsByStatus(status, pageable, currentUser);
//        return ResponseEntity.ok(posts);
//    }
//


}