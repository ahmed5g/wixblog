package com.tech.wixblog.security.oauth2.user;


import com.tech.wixblog.exception.OAuth2AuthenticationProcessingException;
import com.tech.wixblog.model.enums.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId == null || registrationId.isBlank()) {
            throw new OAuth2AuthenticationProcessingException("Registration id must not be blank");
        }
        AuthProvider provider;
        try {
            provider = AuthProvider.valueOf(registrationId.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new OAuth2AuthenticationProcessingException(String.format("Login with %s is not supported.", registrationId));
        }

        switch (provider) {
            case GOOGLE:
                return new GoogleOAuth2UserInfo(attributes);
            case FACEBOOK:
                return new FacebookOAuth2UserInfo(attributes);
            case GITHUB:
                return new GithubOAuth2UserInfo(attributes);
            case LINKEDIN:
                return new LinkedinOAuth2UserInfo(attributes);
            case TWITTER:
                return new TwitterOAuth2UserInfo(attributes);
            default:
                throw new OAuth2AuthenticationProcessingException(String.format("Login with %s is not supported.", registrationId));
        }
    }

}