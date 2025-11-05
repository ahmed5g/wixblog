package com.tech.wixblog.controllers;

import com.tech.wixblog.dto.CommentDTO;
import com.tech.wixblog.dto.CreateCommentDTO;
import com.tech.wixblog.models.User;
import com.tech.wixblog.services.CommentService;
import com.tech.wixblog.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;


    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<Page<CommentDTO>> getPostComments(
            @PathVariable Long postId,
            @ParameterObject Pageable pageable) {
        Page<CommentDTO> comments = commentService.getPostComments(postId, pageable);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentDTO createCommentDTO,
            @AuthenticationPrincipal OAuth2User principal) {
        User author = userService.extractUserFromOAuth2User(principal);
        Optional<CommentDTO> comment = commentService.createComment(postId, createCommentDTO, author);
        return comment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestParam String content,
            @AuthenticationPrincipal OAuth2User principal) {
        User currentUser=userService.extractUserFromOAuth2User(principal);
        Optional<CommentDTO> updatedComment = commentService.updateComment(postId, commentId, content, currentUser);
        return updatedComment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")

    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal OAuth2User oAuth2User) {
        User currentUser=userService.extractUserFromOAuth2User(oAuth2User);
        boolean deleted = commentService.deleteComment(postId, commentId, currentUser);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }


    @GetMapping("/comments/my-comments")

    public ResponseEntity<Page<CommentDTO>> getMyComments(
            @ParameterObject Pageable pageable,
            @AuthenticationPrincipal OAuth2User oAuth2User) {
        User currentUser=userService.extractUserFromOAuth2User(oAuth2User);
        Page<CommentDTO> comments = commentService.getUserComments(currentUser, pageable);
        return ResponseEntity.ok(comments);
    }
}
