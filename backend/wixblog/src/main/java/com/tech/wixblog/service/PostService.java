package com.tech.wixblog.service;

import com.tech.wixblog.dto.post.CreatePostRequest;
import com.tech.wixblog.dto.post.PostResponse;
import com.tech.wixblog.dto.post.UpdatePostRequest;
import com.tech.wixblog.exception.ResourceNotFoundException;
import com.tech.wixblog.exception.UnauthorizedAccessException;
import com.tech.wixblog.mapper.PostMapper;
import com.tech.wixblog.model.Post;
import com.tech.wixblog.model.User;
import com.tech.wixblog.model.enums.PostStatus;
import com.tech.wixblog.repository.PostRepository;
import com.tech.wixblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    private static final String POST_NOT_FOUND = "Post not found with id: ";
    private static final String POST_NOT_FOUND_SLUG = "Post not found with slug: ";
    private static final String USER_NOT_FOUND = "User not found with id: ";
    private static final String UNAUTHORIZED_ACCESS = "You are not authorized to perform this action";

    @Transactional
    public PostResponse createPost(CreatePostRequest request, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + authorId));

        Post post = postMapper.toEntity(request);
        post.setAuthor(author);

        String originalSlug = post.getSlug();
        String uniqueSlug = generateUniqueSlug(originalSlug);
        post.setSlug(uniqueSlug);

        Post savedPost = postRepository.save(post);
        return postMapper.toResponse(savedPost);
    }

    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(POST_NOT_FOUND + id));
        return postMapper.toResponse(post);
    }

    public PostResponse getPostBySlug(String slug) {
        Post post = postRepository.findBySlugAndStatus(slug, PostStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException(POST_NOT_FOUND_SLUG + slug));
        return postMapper.toResponse(post);
    }

    @Transactional
    public PostResponse updatePost(Long id, UpdatePostRequest request, Long currentUserId) {
        Post post = findPostById(id);
        validatePostOwnership(post, currentUserId);

        postMapper.updateEntityFromRequest(request, post);

        if (request.getTitle() != null && !request.getTitle().equals(post.getTitle())) {
            String uniqueSlug = generateUniqueSlug(post.getSlug());
            post.setSlug(uniqueSlug);
        }

        Post updatedPost = postRepository.save(post);
        return postMapper.toResponse(updatedPost);
    }

    @Transactional
    public void deletePost(Long id, Long currentUserId) {
        Post post = findPostById(id);
        validatePostOwnership(post, currentUserId);
        postRepository.delete(post);
    }

    public Page<PostResponse> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(postMapper::toResponse);
    }

    public Page<PostResponse> getPublishedPosts(Pageable pageable) {
        return postRepository.findByStatus(PostStatus.PUBLISHED, pageable)
                .map(postMapper::toResponse);
    }

    public Page<PostResponse> getUserPosts(Long userId, Pageable pageable) {
        return postRepository.findAllByAuthorId(userId, pageable)
                .map(postMapper::toResponse);
    }



    public Page<PostResponse> searchPosts(String query, Pageable pageable) {
        return postRepository.searchPublishedPosts(query, PostStatus.PUBLISHED, pageable)
                .map(postMapper::toResponse);
    }

    @Transactional
    public PostResponse changePostStatus(Long id, PostStatus status, Long currentUserId) {
        Post post = findPostById(id);
        validatePostOwnership(post, currentUserId);

        post.setStatus(status);
        Post updatedPost = postRepository.save(post);
        return postMapper.toResponse(updatedPost);
    }



    public boolean existsBySlug(String slug) {
        return postRepository.findBySlugAndStatus(slug, null).isPresent();
    }

    public Long getUserPostCount(Long userId) {
        return postRepository.countTotalPostsByAuthorId(userId);
    }

    public Long getUserPublishedPostCount(Long userId) {
        return postRepository.countByAuthorIdAndStatus(userId, PostStatus.PUBLISHED);
    }

    private Post findPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(POST_NOT_FOUND + id));
    }

    private void validatePostOwnership(Post post, Long currentUserId) {
        if (!post.getAuthor().getId().equals(currentUserId)) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS);
        }
    }

    private String generateUniqueSlug(String baseSlug) {
        String slug = baseSlug;
        int counter = 1;

        while (existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }

    @Transactional
    public void incrementViewCount(Long postId) {
        Post post = findPostById(postId);
        post.incrementViewCount();
        postRepository.save(post);
    }


    @Transactional
    public boolean toggleLike(Long postId, Long userId) {
        Post post = findPostById(postId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + userId));

        boolean liked = post.toggleLike(user);
        postRepository.save(post);
        return liked;
    }


    public boolean isPostLikedByUser(Long postId, Long userId) {
        Post post = findPostById(postId);
        return post.isLikedByUserId(userId);
    }

    public Long getPostLikeCount(Long postId) {
        Post post = findPostById(postId);
        return post.getLikeCount();
    }


}