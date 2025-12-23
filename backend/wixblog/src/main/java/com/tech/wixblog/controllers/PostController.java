package com.tech.wixblog.controllers;

import com.tech.wixblog.dto.post.CreatePostRequest;
import com.tech.wixblog.dto.post.PostResponse;
import com.tech.wixblog.dto.post.UpdatePostRequest;
import com.tech.wixblog.model.enums.PostStatus;
import com.tech.wixblog.security.CurrentUser;
import com.tech.wixblog.security.UserPrincipal;
import com.tech.wixblog.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Post management APIs")
public class PostController {
    private final PostService postService;

    @PostMapping
    @Operation(summary = "Create a new post")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<PostResponse> createPost (
            @Valid @RequestBody CreatePostRequest request,
            @CurrentUser UserPrincipal currentUser) {
        PostResponse response = postService.createPost(request, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get post by ID (only published)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post found"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<PostResponse> getPostById (@PathVariable Long id) {
        PostResponse response = postService.getPostById(id);
        postService.incrementViewCount(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get post by slug")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post found"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<PostResponse> getPostBySlug (@PathVariable String slug) {
        PostResponse response = postService.getPostBySlug(slug);
        // Increment view count when accessed by slug too
        postService.incrementViewCount(response.getId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<PostResponse> updatePost (
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostRequest request,
            @CurrentUser UserPrincipal currentUser) {
        PostResponse response = postService.updatePost(id, request, currentUser.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete post")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<Void> deletePost (
            @PathVariable Long id,
            @CurrentUser UserPrincipal currentUser) {
        postService.deletePost(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all posts (paginated)")
    public ResponseEntity<Page<PostResponse>> getAllPosts (
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        Page<PostResponse> response = postService.getAllPosts(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/published")
    @Operation(summary = "Get published posts (paginated)")
    public ResponseEntity<Page<PostResponse>> getPublishedPosts (
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        Page<PostResponse> response = postService.getPublishedPosts(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-posts")
    @Operation(summary = "Get current user's posts")
    public ResponseEntity<Page<PostResponse>> getMyPosts (
            @ParameterObject @PageableDefault(size = 20) Pageable pageable,
            @CurrentUser UserPrincipal currentUser) {
        Page<PostResponse> response = postService.getUserPosts(currentUser.getId(), pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search published posts")
    public ResponseEntity<Page<PostResponse>> searchPosts (
            @RequestParam String query,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        Page<PostResponse> response = postService.searchPosts(query, pageable);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change post status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<PostResponse> changePostStatus (
            @PathVariable Long id,
            @RequestParam PostStatus status,
            @CurrentUser UserPrincipal currentUser) {
        PostResponse response = postService.changePostStatus(id, status, currentUser.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "Toggle like on post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Like toggled successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<Map<String, Object>> toggleLike (
            @PathVariable Long id,
            @CurrentUser UserPrincipal currentUser) {
        boolean liked = postService.toggleLike(id, currentUser.getId());
        Long likeCount = postService.getPostLikeCount(id);
        boolean isLiked = postService.isPostLikedByUser(id, currentUser.getId());
        Map<String, Object> response = Map.of(
                "action", liked ? "liked" : "unliked",
                "liked", liked,
                "likeCount", likeCount,
                "isLiked", isLiked
                                             );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/like-status")
    @Operation(summary = "Get post like status for current user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<Map<String, Object>> getLikeStatus (
            @PathVariable Long id,
            @CurrentUser UserPrincipal currentUser) {
        Long likeCount = postService.getPostLikeCount(id);
        boolean isLiked = postService.isPostLikedByUser(id, currentUser.getId());
        Map<String, Object> response = Map.of(
                "likeCount", likeCount,
                "isLiked", isLiked
                                             );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/like-count")
    @Operation(summary = "Get post like count")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Count retrieved"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<Map<String, Long>> getLikeCount (@PathVariable Long id) {
        Long likeCount = postService.getPostLikeCount(id);
        return ResponseEntity.ok(Map.of("likeCount", likeCount));
    }

    @GetMapping("/{id}/view-count")
    @Operation(summary = "Get post view count")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Count retrieved"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<Map<String, Long>> getViewCount (@PathVariable Long id) {
        PostResponse post = postService.getPostById(id);
        Long viewCount = post.getViewCount();
        return ResponseEntity.ok(Map.of("viewCount", viewCount));
    }

    @PostMapping("/{id}/increment-view")
    @Operation(summary = "Manually increment view count")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "View count incremented"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<Map<String, Long>> incrementViewCount (@PathVariable Long id) {
        postService.incrementViewCount(id);
        PostResponse post = postService.getPostById(id);
        return ResponseEntity.ok(Map.of("viewCount", post.getViewCount()));
    }

    @GetMapping("/stats")
    @Operation(summary = "Get user post statistics")
    public ResponseEntity<Map<String, Long>> getUserPostStats (
            @CurrentUser UserPrincipal currentUser) {
        Long totalPosts = postService.getUserPostCount(currentUser.getId());
        Long publishedPosts = postService.getUserPublishedPostCount(currentUser.getId());
        Map<String, Long> stats = Map.of(
                "totalPosts", totalPosts,
                "publishedPosts", publishedPosts
                                        );
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/category/{categorySlug}")
    public Page<PostResponse> getPostsByCategory (
            @PathVariable String categorySlug,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return postService.getPostsByCategory(categorySlug, pageable);
    }

    @GetMapping("/category/{categorySlug}/filter")
    public Page<PostResponse> getPostsByCategoryAndTags (
            @PathVariable String categorySlug,
            @RequestParam Set<String> tags,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return postService.getPostsByCategoryAndTags(categorySlug, tags, pageable);
    }

    @GetMapping("/tag/{tagSlug}")
    public Page<PostResponse> getPostsByTag (
            @PathVariable String tagSlug,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return postService.getPostsByTag(tagSlug, pageable);
    }

    @PutMapping("/{id}/tags")
    @Operation(summary = "Update post tags")
    public PostResponse updatePostTags (
            @PathVariable Long id,
            @RequestParam Set<String> newTagNames) {
        return postService.updatePostTags(id, newTagNames);
    }


}