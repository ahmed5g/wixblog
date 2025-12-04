package com.tech.wixblog.service;

import com.tech.wixblog.dto.comment.CommentResponse;
import com.tech.wixblog.dto.comment.CreateCommentRequest;
import com.tech.wixblog.dto.comment.UpdateCommentRequest;
import com.tech.wixblog.exception.ResourceNotFoundException;
import com.tech.wixblog.exception.UnauthorizedAccessException;
import com.tech.wixblog.mapper.CommentMapper;
import com.tech.wixblog.model.Comment;
import com.tech.wixblog.model.Post;
import com.tech.wixblog.model.User;
import com.tech.wixblog.model.enums.CommentStatus;
import com.tech.wixblog.repository.CommentRepository;
import com.tech.wixblog.repository.PostRepository;
import com.tech.wixblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService  {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    private static final String COMMENT_NOT_FOUND = "Comment not found with id: ";
    private static final String POST_NOT_FOUND = "Post not found with id: ";
    private static final String USER_NOT_FOUND = "User not found with id: ";
    private static final String PARENT_COMMENT_NOT_FOUND = "Parent comment not found with id: ";
    private static final String UNAUTHORIZED_ACCESS = "You are not authorized to perform this action";

    @Transactional
    public CommentResponse createComment(CreateCommentRequest request, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + authorId));

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException(POST_NOT_FOUND + request.getPostId()));

        Comment comment = commentMapper.toEntity(request);
        comment.setAuthor(author);
        comment.setPost(post);

        if (request.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException(PARENT_COMMENT_NOT_FOUND + request.getParentCommentId()));
            comment.setParentComment(parentComment);
        }

        Comment savedComment = commentRepository.save(comment);

        post.incrementCommentCount();
        postRepository.save(post);

        return commentMapper.toResponse(savedComment);
    }

    public CommentResponse getCommentById(Long id) {
        Comment comment = commentRepository.findByIdAndStatus(id, CommentStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND + id));
        return commentMapper.toResponse(comment);
    }


    @Transactional
    public CommentResponse updateComment(Long id, UpdateCommentRequest request, Long currentUserId) {
        Comment comment = findCommentById(id);
        validateCommentOwnership(comment, currentUserId);

        commentMapper.updateEntityFromRequest(request, comment);
        Comment updatedComment = commentRepository.save(comment);

        return commentMapper.toResponse(updatedComment);
    }


    @Transactional(readOnly = true)
    public void deleteComment(Long id, Long currentUserId) {
        Comment comment = findCommentById(id);
        validateCommentOwnership(comment, currentUserId);

        comment.setStatus(CommentStatus.DELETED);
        commentRepository.save(comment);

        Post post = comment.getPost();
        post.decrementCommentCount();
        postRepository.save(post);
    }


    public Page<CommentResponse> getCommentsByPost(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(POST_NOT_FOUND + postId));

        return commentRepository.findByPostAndStatusAndParentCommentIsNull(post, CommentStatus.ACTIVE, pageable)
                .map(commentMapper::toResponse);
    }


    public Page<CommentResponse> getCommentsByUser(Long userId, Pageable pageable) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + userId));

        return commentRepository.findByAuthorAndStatus(author, CommentStatus.ACTIVE, pageable)
                .map(commentMapper::toResponse);
    }

    public List<CommentResponse> getCommentReplies(Long parentCommentId) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND + parentCommentId));

        List<Comment> replies = commentRepository.findByParentCommentAndStatusOrderByCreatedAtAsc(
                parentComment, CommentStatus.ACTIVE);

        return commentMapper.toResponseList(replies);
    }

    public List<CommentResponse> getPostCommentsWithReplies(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(POST_NOT_FOUND + postId));

        List<Comment> topLevelComments = commentRepository.findTopLevelCommentsByPostWithAuthor(post, CommentStatus.ACTIVE);

        return topLevelComments.stream()
                .map(comment -> {
                    CommentResponse response = commentMapper.toResponse(comment);
                    List<CommentResponse> replies = getCommentReplies(comment.getId());
                    response.setReplies(replies);
                    response.setReplyCount(replies.size());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CommentResponse changeCommentStatus(Long id, CommentStatus status, Long currentUserId) {
        Comment comment = findCommentById(id);
        validateCommentOwnership(comment, currentUserId);

        comment.setStatus(status);
        Comment updatedComment = commentRepository.save(comment);

        return commentMapper.toResponse(updatedComment);
    }


    @Transactional(readOnly = true)
    public void incrementLikeCount(Long id) {
        Comment comment = findCommentById(id);
        comment.incrementLikeCount();
        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public void decrementLikeCount(Long id) {
        Comment comment = findCommentById(id);
        comment.decrementLikeCount();
        commentRepository.save(comment);
    }

    public Long getPostCommentCount(Long postId) {
        return commentRepository.countByPostIdAndStatus(postId, CommentStatus.ACTIVE);
    }


    public Long getUserCommentCount(Long userId) {
        return commentRepository.countTotalCommentsByAuthorId(userId);
    }

    private Comment findCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND + id));
    }

    private void validateCommentOwnership(Comment comment, Long currentUserId) {
        if (!comment.getAuthor().getId().equals(currentUserId)) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS);
        }
    }
}