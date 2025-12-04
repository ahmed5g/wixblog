package com.tech.wixblog.service;

import com.tech.wixblog.model.User;
import com.tech.wixblog.model.enums.AuthProvider;
import com.tech.wixblog.security.oauth2.user.OAuth2UserInfo;

public interface OAuthUserService {
    User processOAuthUser(AuthProvider provider, OAuth2UserInfo userInfo);
    User findOrCreateUser(AuthProvider provider, OAuth2UserInfo userInfo);
    User findUserByOAuthProvider(AuthProvider provider, String providerUserId);
    User findUserByEmail(String email);
    User linkOAuthToExistingUser(User user, AuthProvider provider, OAuth2UserInfo userInfo);
    User createNewOAuthUser(AuthProvider provider, OAuth2UserInfo userInfo);
    void updateUserProfile(User user, OAuth2UserInfo userInfo);
}