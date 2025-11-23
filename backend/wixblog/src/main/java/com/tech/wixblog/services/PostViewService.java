package com.tech.wixblog.services;

import com.tech.wixblog.dto.PostViewDTO;
import com.tech.wixblog.mapper.PostViewMapper;
import com.tech.wixblog.model.PostView;
import com.tech.wixblog.model.User;
import com.tech.wixblog.repositories.PostRepository;
import com.tech.wixblog.repositories.PostViewRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostViewService {
    private final PostViewRepository postViewRepository;
    private final PostRepository postRepository;
    private final PostViewMapper postViewMapper;

    @Transactional
    public Optional<PostViewDTO> trackPostView (Long postId,
                                                User user,
                                                HttpServletRequest request) {
        return postRepository.findById(postId)
                .map(post -> {
                    // Check if user already viewed this post (optional - for unique views)
                    if (user != null && postViewRepository.existsByPostAndUser(post, user)) {
                        return Optional.<PostViewDTO>empty();
                    }
                    PostView postView = new PostView();
                    postView.setPost(post);
                    postView.setUser(user);
                    postView.setIpAddress(getClientIpAddress(request));
                    postView.setUserAgent(request.getHeader("User-Agent"));
                    PostView savedView = postViewRepository.save(postView);
                    // Update view count in post
                    post.setViewCount(post.getViewCount() + 1);
                    postRepository.save(post);
                    return Optional.of(postViewMapper.toDTO(savedView));
                })
                .orElse(Optional.empty());
    }

    @Transactional(readOnly = true)
    public Long getPostViewCount (Long postId) {
        return postRepository.findById(postId)
                .map(postViewRepository::countByPost)
                .orElse(0L);
    }

    @Transactional(readOnly = true)
    public Page<PostViewDTO> getPostViewsStats (Long postId, Pageable pageable) {
        return postRepository.findById(postId)
                .map(post -> postViewRepository.findByPost(post, pageable)
                        .map(postViewMapper::toDTO))
                .orElse(Page.empty());
    }

    @Transactional(readOnly = true)
    public boolean isPostViewedByUser (Long postId, User user) {
        return postRepository.findById(postId)
                .map(post -> postViewRepository.existsByPostAndUser(post, user))
                .orElse(false);
    }

    /**
     * ============================
     * HELPER METHODS
     * ============================
     */
    private String getClientIpAddress (HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}