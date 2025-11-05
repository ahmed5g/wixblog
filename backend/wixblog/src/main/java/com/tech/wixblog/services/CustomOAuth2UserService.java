package com.tech.wixblog.services;

import com.tech.wixblog.models.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        logger.info("OAuth2 login process started");
        OAuth2User oAuth2User = super.loadUser(userRequest);
        logger.info("OAuth2 user loaded: {}", oAuth2User.getAttributes());

        return processOAuth2User(oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String profilePicture = oAuth2User.getAttribute("picture");

        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        // Find or create user in database
        User user = userService.findOrCreateUser(email, name, profilePicture);

        // Create Spring Security User with authorities
        return createSpringSecurityUser(oAuth2User, user);
    }

    private OAuth2User createSpringSecurityUser(OAuth2User oAuth2User, User user) {
        Collection<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().name()) // Creates "USER" or "ADMIN"
        );

        // Add custom attributes
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("userId", user.getId());
        attributes.put("role", user.getRole());

        return new DefaultOAuth2User(authorities, attributes, "email");
    }
}