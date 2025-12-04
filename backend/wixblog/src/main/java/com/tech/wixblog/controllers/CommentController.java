package com.tech.wixblog.controllers;

import com.tech.wixblog.dto.comment.CommentResponse;
import com.tech.wixblog.dto.comment.CreateCommentRequest;
import com.tech.wixblog.dto.comment.UpdateCommentRequest;
import com.tech.wixblog.model.enums.CommentStatus;
import com.tech.wixblog.security.CurrentUser;
import com.tech.wixblog.security.UserPrincipal;
import com.tech.wixblog.service.CommentService;
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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Comment management APIs")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "Create a new comment")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Post or parent comment not found")
    })
    public ResponseEntity<CommentResponse> createComment (
            @Valid @RequestBody CreateCommentRequest request,
            @CurrentUser UserPrincipal currentUser) {
        CommentResponse response = commentService.createComment(request, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get comment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment found"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    public ResponseEntity<CommentResponse> getCommentById (@PathVariable Long id) {
        CommentResponse response = commentService.getCommentById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update comment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    public ResponseEntity<CommentResponse> updateComment (
            @PathVariable Long id,
            @Valid @RequestBody UpdateCommentRequest request,
            @CurrentUser UserPrincipal currentUser) {
        CommentResponse response = commentService.updateComment(id, request, currentUser.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete comment")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    public ResponseEntity<Void> deleteComment (
            @PathVariable Long id,
            @CurrentUser UserPrincipal currentUser) {
        commentService.deleteComment(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "Get comments by post (paginated)")
    public ResponseEntity<Page<CommentResponse>> getCommentsByPost (
            @PathVariable Long postId,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        Page<CommentResponse> response = commentService.getCommentsByPost(postId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/post/{postId}/with-replies")
    @Operation(summary = "Get all comments with replies for a post")
    public ResponseEntity<List<CommentResponse>> getPostCommentsWithReplies (
            @PathVariable Long postId) {
        List<CommentResponse> response = commentService.getPostCommentsWithReplies(postId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get comments by user (paginated)")
    public ResponseEntity<Page<CommentResponse>> getCommentsByUser (
            @PathVariable Long userId,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        Page<CommentResponse> response = commentService.getCommentsByUser(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/replies")
    @Operation(summary = "Get comment replies")
    public ResponseEntity<List<CommentResponse>> getCommentReplies (@PathVariable Long id) {
        List<CommentResponse> response = commentService.getCommentReplies(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change comment status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    public ResponseEntity<CommentResponse> changeCommentStatus (
            @PathVariable Long id,
            @RequestParam CommentStatus status,
            @CurrentUser UserPrincipal currentUser) {
        CommentResponse response = commentService.changeCommentStatus(id, status,
                                                                      currentUser.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "Increment comment like count")
    public ResponseEntity<Void> incrementLikeCount (@PathVariable Long id) {
        commentService.incrementLikeCount(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/unlike")
    @Operation(summary = "Decrement comment like count")
    public ResponseEntity<Void> decrementLikeCount (@PathVariable Long id) {
        commentService.decrementLikeCount(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    @Operation(summary = "Get user comment statistics")
    public ResponseEntity<Map<String, Long>> getUserCommentStats (
            @CurrentUser UserPrincipal currentUser) {
        Long totalComments = commentService.getUserCommentCount(currentUser.getId());
        Map<String, Long> stats = Map.of("totalComments", totalComments);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/post/{postId}/count")
    @Operation(summary = "Get post comment count")
    public ResponseEntity<Map<String, Long>> getPostCommentCount (@PathVariable Long postId) {
        Long commentCount = commentService.getPostCommentCount(postId);
        Map<String, Long> stats = Map.of("commentCount", commentCount);
        return ResponseEntity.ok(stats);
    }
}