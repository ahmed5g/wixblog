package com.tech.wixblog.services;

import com.tech.wixblog.dto.UpdateProfileDTO;
import com.tech.wixblog.dto.UpdateProfileStatsDTO;
import com.tech.wixblog.dto.UpdateSettingsDTO;
import com.tech.wixblog.dto.UserDTO;
import com.tech.wixblog.dto.payload.UserResponse;
import com.tech.wixblog.mapper.UserMapper;
import com.tech.wixblog.model.Role;
import com.tech.wixblog.model.User;
import com.tech.wixblog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse getUserInfoById (Long id) {
        log.debug("Getting user info by id: {}", id);

        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: %s.".formatted(id)));

        return mapToUserResponse(user);
    }

    private static UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setName(user.getName());
        userResponse.setImageUrl(user.getProfilePicture());
        return userResponse;
    }

}