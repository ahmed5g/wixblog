package com.tech.wixblog.services;

import com.tech.wixblog.dto.LikeDTO;
import com.tech.wixblog.dto.payload.UserResponse;
import com.tech.wixblog.mapper.LikeMapper;
import com.tech.wixblog.mapper.UserMapper;
import com.tech.wixblog.model.Like;
import com.tech.wixblog.model.Post;
import com.tech.wixblog.model.User;
import com.tech.wixblog.repositories.LikeRepository;
import com.tech.wixblog.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final LikeMapper likeMapper;

    private final UserMapper userMapper;
    
    @Transactional
    public Optional<LikeDTO> likePost(Long postId, User user) {
        return postRepository.findById(postId)
                .map(post -> {

                    if (likeRepository.existsByPostAndUser(post, user)) {
                        return Optional.<LikeDTO>empty();
                    }
                    
                    Like like = new Like();
                    like.setPost(post);
                    like.setUser(user);
                    
                    Like savedLike = likeRepository.save(like);
                    updateLikeCount(post);
                    
                    return Optional.of(likeMapper.toDTO(savedLike));
                })
                .orElse(Optional.empty());
    }

    @Transactional
    public boolean unlikePost(Long postId, User user) {
        return postRepository.findById(postId)
                .map(post -> {
                    Optional<Like> existingLike = likeRepository.findByPostAndUser(post, user);
                    if (existingLike.isPresent()) {
                        likeRepository.delete(existingLike.get());
                        updateLikeCount(post);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }


    @Transactional(readOnly = true)
    public Long getPostLikeCount(Long postId) {
        return postRepository.findById(postId)
                .map(likeRepository::countByPost)
                .orElse(0L);
    }


    @Transactional(readOnly = true)
    public boolean isPostLikedByUser(Long postId, User user) {
        return postRepository.findById(postId)
                .map(post -> likeRepository.existsByPostAndUser(post, user))
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getUsersWhoLikedPost (Long postId, Pageable pageable) {
        return postRepository.findById(postId)
                .map(post -> likeRepository.findUsersByPost(post, pageable)
                        .map(userMapper::userToUserResponse))
                .orElse(Page.empty(pageable));
    }
    


    /*HELPER METHODS*/
    private void updateLikeCount(Post post) {
        Long likeCount = likeRepository.countByPost(post);
        post.setLikeCount(likeCount);
        postRepository.save(post);
    }


}