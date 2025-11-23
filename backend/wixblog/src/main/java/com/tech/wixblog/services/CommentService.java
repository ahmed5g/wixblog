package com.tech.wixblog.services;

import com.tech.wixblog.dto.CommentDTO;
import com.tech.wixblog.dto.CreateCommentDTO;
import com.tech.wixblog.mapper.CommentMapper;
import com.tech.wixblog.model.Comment;
import com.tech.wixblog.model.Post;
import com.tech.wixblog.model.User;
import com.tech.wixblog.repositories.CommentRepository;
import com.tech.wixblog.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    public Page<CommentDTO> getPostComments(Long postId, Pageable pageable) {
        return postRepository.findById(postId)
                .map(post -> {
                    Page<Comment> comments = commentRepository.findByPostAndParentCommentIsNullOrderByCreatedAtDesc(post, pageable);
                    return comments.map(this::buildCommentTree);
                })
                .orElse(Page.empty());
    }

    @Transactional
    public Optional<CommentDTO> createComment(Long postId, CreateCommentDTO createCommentDTO, User author) {
        return postRepository.findById(postId)
                .map(post -> {
                    if (!post.getAllowComments()) {
                        throw new IllegalStateException("Comments are not allowed for this post");
                    }

                    Comment comment = commentMapper.toEntity(createCommentDTO);
                    comment.setPost(post);
                    comment.setAuthor(author);

                    // Set parent comment if provided
                    if (createCommentDTO.getParentCommentId() != null) {
                        commentRepository.findById(createCommentDTO.getParentCommentId())
                                .ifPresent(comment::setParentComment);
                    }

                    Comment savedComment = commentRepository.save(comment);
                    updateCommentCount(post);

                    return buildCommentTree(savedComment);
                });
    }

    @Transactional(readOnly = true)
    public Page<CommentDTO> getUserComments(User author, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByAuthorOrderByCreatedAtDesc(author, pageable);
        return comments.map(commentMapper::toDTO);
    }

    @Transactional
    public Optional<CommentDTO> updateComment(Long postId, Long commentId, String content, User currentUser) {
        return commentRepository.findById(commentId)
                .map(comment -> {

                    if (!comment.getPost().getId().equals(postId)) {
                        throw new IllegalArgumentException("Comment does not belong to the specified post");
                    }


                    if (!comment.getAuthor().getId().equals(currentUser.getId()) &&
                            !currentUser.getRole().name().equals("ROLE_ADMIN")) {
                        throw new AccessDeniedException("you are not allowed to update this " +
                                                                "comment");
                    }

                    comment.setContent(content);
                    Comment updatedComment = commentRepository.save(comment);
                    return commentMapper.toDTO(updatedComment);
                });
    }

    @Transactional
    public boolean deleteComment(Long postId, Long commentId, User currentUser) {
        return commentRepository.findById(commentId)
                .map(comment -> {

                    if (!comment.getPost().getId().equals(postId)) {
                        throw new IllegalArgumentException("Comment does not belong to the specified post");
                    }


                    if (!comment.getAuthor().getId().equals(currentUser.getId()) &&
                            !currentUser.getRole().name().equals("ROLE_ADMIN")) {
                        throw new AccessDeniedException("you are not allowed to delete this " +
                                                                "comment");
                    }

                    Post post = comment.getPost();
                    commentRepository.delete(comment);
                    updateCommentCount(post);
                    return true;
                })
                .orElse(false);
    }



    /* HELPER METHODS */
    private void updateCommentCount(Post post) {
        Long commentCount = commentRepository.countApprovedCommentsByPost(post);
        post.setCommentCount(commentCount);
        postRepository.save(post);
    }

    private CommentDTO buildCommentTree(Comment comment) {
        CommentDTO commentDTO = commentMapper.toDTO(comment);

        List<Comment> replies = commentRepository.findByParentCommentOrderByCreatedAtAsc(comment);
        List<CommentDTO> replyDTOs = replies.stream()
                .map(this::buildCommentTree)
                .collect(Collectors.toList());

        commentDTO.setReplies(replyDTOs);
        return commentDTO;
    }


    @Transactional
    public Optional<CommentDTO> approveComment(Long commentId) {
        return commentRepository.findById(commentId)
                .map(comment -> {
                    comment.setIsApproved(true);
                    Comment approvedComment = commentRepository.save(comment);
                    return commentMapper.toDTO(approvedComment);
                });
    }

}
