package com.tech.wixblog.services;

import com.tech.wixblog.dto.CreatePostDTO;
import com.tech.wixblog.dto.PostDTO;
import com.tech.wixblog.dto.UpdatePostDTO;
import com.tech.wixblog.mapper.PostMapper;
import com.tech.wixblog.model.Post;
import com.tech.wixblog.model.PostStatus;
import com.tech.wixblog.model.User;
import com.tech.wixblog.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final LikeService likeService;
    private final PostViewService postViewService;

    @Transactional(readOnly = true)
    public Page<PostDTO> getPublishedPosts (Pageable pageable, User currentUser) {
        Page<Post> posts = postRepository.findByStatusOrderByCreatedAtDesc(PostStatus.PUBLISHED,
                                                                           pageable);
        return posts.map(post -> enrichPostDTO(postMapper.toDTO(post), currentUser));
    }

    @Transactional(readOnly = true)
    public Optional<PostDTO> getPublishedPostBySlug (String slug, User currentUser) {
        return postRepository.findBySlugAndStatus(slug, PostStatus.PUBLISHED).map(
                post -> enrichPostDTO(postMapper.toDTO(post), currentUser));
    }


    @Transactional
    public List<PostDTO> createPosts(List<CreatePostDTO> createPostDTOs, User author) {
        List<Post> posts = createPostDTOs.stream()
                .map(dto -> {
                    Post post = postMapper.toEntity(dto);
                    post.setAuthor(author);
                    if (post.getStatus() == PostStatus.PUBLISHED && post.getPublishedAt() == null) {
                        post.setPublishedAt(java.time.LocalDateTime.now());
                    }
                    post.calculateMetrics();
                    return post;
                })
                .collect(Collectors.toList());

        List<Post> savedPosts = postRepository.saveAll(posts);

        return savedPosts.stream()
                .map(post -> enrichPostDTO(postMapper.toDTO(post), author))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PostDTO> updatePosts(List<UpdatePostDTO> updatePostDTOs, User currentUser) {
        // Group by postId to handle multiple updates for the same post
        Map<Long, UpdatePostDTO> updatesByPostId = updatePostDTOs.stream()
                .filter(dto -> dto.getPostId() != null)
                .collect(Collectors.toMap(
                        UpdatePostDTO::getPostId,
                        dto -> dto,
                        (existing, replacement) -> {
                            // Merge updates if same post appears multiple times
                            // You might want more sophisticated merging logic here
                            return replacement;
                        }
                                         ));

        // Fetch all posts in one query
        List<Long> postIds = new ArrayList<>(updatesByPostId.keySet());
        List<Post> posts = postRepository.findAllById(postIds);

        // Update posts
        List<Post> updatedPosts = posts.stream()
                .map(post -> {
                    UpdatePostDTO updateDTO = updatesByPostId.get(post.getId());

                    // Check permissions for each post
                    if (!post.getAuthor().getId().equals(currentUser.getId()) &&
                            !currentUser.getRole().name().equals("ROLE_ADMIN")) {
                        throw new AccessDeniedException("You can only update your own posts. Post ID: " + post.getId());
                    }

                    // Apply updates using your existing mapper
                    postMapper.updateEntityFromDTO(updateDTO, post);

                    // Handle special logic
                    if (updateDTO.getStatus() == PostStatus.PUBLISHED && post.getPublishedAt() == null) {
                        post.setPublishedAt(LocalDateTime.now());
                    }
                    if (updateDTO.getContent() != null) {
                        post.calculateMetrics();
                    }

                    return post;
                })
                .collect(Collectors.toList());

        // Save all updated posts
        List<Post> savedPosts = postRepository.saveAll(updatedPosts);

        // Convert to DTOs and enrich
        return savedPosts.stream()
                .map(post -> enrichPostDTO(postMapper.toDTO(post), currentUser))
                .collect(Collectors.toList());
    }

    // Keep your existing single update method
    @Transactional
    public Optional<PostDTO> updatePost(Long postId, UpdatePostDTO updatePostDTO, User currentUser) {
        return postRepository.findById(postId).map(post -> {
            if (!post.getAuthor().getId().equals(currentUser.getId()) &&
                    !currentUser.getRole().name().equals("ROLE_ADMIN")) {
                throw new AccessDeniedException("You can only update your own posts");
            }
            postMapper.updateEntityFromDTO(updatePostDTO, post);
            post.calculateMetrics();
            Post updatedPost = postRepository.save(post);
            return enrichPostDTO(postMapper.toDTO(updatedPost), currentUser);
        });
    }

    @Transactional
    public boolean deletePost (Long postId, User user) {
        return postRepository.findById(postId).map(post -> {
            // Check if user owns the post or is admin
            if (!post.getAuthor().getId().equals(user.getId()) && !user.getRole().name().equals(
                    "ROLE_ADMIN")) {
                throw new AccessDeniedException("You can only delete your own posts");
            }
            postRepository.delete(post);
            return true;
        }).orElse(false);
    }

    @Transactional(readOnly = true)
    public Page<PostDTO> searchPosts (String query, Pageable pageable, User currentUser) {
        Page<Post> posts = postRepository.searchPosts(query, PostStatus.PUBLISHED, pageable);
        return posts.map(post -> enrichPostDTO(postMapper.toDTO(post), currentUser));
    }

    @Transactional(readOnly = true)
    public Page<PostDTO> getFeaturedPosts (Pageable pageable, User currentUser) {
        List<Post> featuredPosts = postRepository.findByIsFeaturedTrueAndStatusOrderByCreatedAtDesc(
                PostStatus.PUBLISHED, pageable);
        Page<Post> posts = new PageImpl<>(featuredPosts, pageable, featuredPosts.size());
        return posts.map(post -> enrichPostDTO(postMapper.toDTO(post), currentUser));
    }

    @Transactional(readOnly = true)
    public Page<PostDTO> getUserPosts (User author, Pageable pageable, User user) {
        Page<Post> posts = postRepository.findByAuthorOrderByCreatedAtDesc(author, pageable);
        return posts.map(post -> enrichPostDTO(postMapper.toDTO(post), user));
    }

    @Transactional
    public Optional<PostDTO> publishPost(Long postId, User currentUser) {
        return postRepository.findById(postId).map(post -> {
            // Check if user owns the post or is admin
            if (!post.getAuthor().getId().equals(currentUser.getId()) &&
                    !currentUser.getRole().name().equals("ROLE_ADMIN")) {
                throw new AccessDeniedException("You can only publish your own posts");
            }

            // Set post as published
            post.setStatus(PostStatus.PUBLISHED);

            // If this is the first time publishing, set published date
            if (post.getPublishedAt() == null) {
                post.setPublishedAt(java.time.LocalDateTime.now());
            }

            Post publishedPost = postRepository.save(post);
            return enrichPostDTO(postMapper.toDTO(publishedPost), currentUser);
        });
    }

    @Transactional
    public Optional<PostDTO> unpublishPost(Long postId, User currentUser) {
        return postRepository.findById(postId).map(post -> {
            // Check if user owns the post or is admin
            if (!post.getAuthor().getId().equals(currentUser.getId()) &&
                    !currentUser.getRole().name().equals("ROLE_ADMIN")) {
                throw new AccessDeniedException("You can only unpublish your own posts");
            }

            // Set post back to draft
            post.setStatus(PostStatus.DRAFT);
            Post unpublishedPost = postRepository.save(post);
            return enrichPostDTO(postMapper.toDTO(unpublishedPost), currentUser);
        });
    }

    // In PostService
    @Transactional(readOnly = true)
    public Page<PostDTO> getUserDrafts(User author, Pageable pageable, User currentUser) {
        Page<Post> drafts = postRepository.findByAuthorAndStatusOrderByCreatedAtDesc(
                author, PostStatus.DRAFT, pageable);
        return drafts.map(post -> enrichPostDTO(postMapper.toDTO(post), currentUser));
    }

    @Transactional(readOnly = true)
    public Page<PostDTO> getPostsByStatus(PostStatus status, Pageable pageable, User currentUser) {
        Page<Post> posts;

        // If user is admin, show all posts of this status
        if (currentUser.getRole().name().equals("ROLE_ADMIN")) {
            posts = postRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        } else {
            // Otherwise, only show user's own posts
            posts = postRepository.findByAuthorAndStatusOrderByCreatedAtDesc(
                    currentUser, status, pageable);
        }

        return posts.map(post -> enrichPostDTO(postMapper.toDTO(post), currentUser));
    }

    /**
     * ============================
     * HELPER METHODS
     * ============================
     */
    // Enrich DTO with additional information
    private PostDTO enrichPostDTO (PostDTO postDTO, User currentUser) {
        if (currentUser != null) {
            postDTO.setLikedByCurrentUser(
                    likeService.isPostLikedByUser(postDTO.getId(), currentUser));
            postDTO.setViewedByCurrentUser(
                    postViewService.isPostViewedByUser(postDTO.getId(), currentUser));
        }
        return postDTO;
    }
}
