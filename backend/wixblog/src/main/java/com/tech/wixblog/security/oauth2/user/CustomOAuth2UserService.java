package com.tech.wixblog.security.oauth2.user;

import com.tech.wixblog.exception.OAuth2AuthenticationProcessingException;
import com.tech.wixblog.model.User;
import com.tech.wixblog.model.enums.AuthProvider;
import com.tech.wixblog.repositories.UserRepository;
import com.tech.wixblog.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser (OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    //todo decompose
    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest,
                                         OAuth2User oAuth2User) {
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                registrationId,
                oAuth2User.getAttributes()
                                                                               );

        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            log.error("Email not found from OAuth2 provider: {}", registrationId);
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        AuthProvider provider = AuthProvider.valueOf(registrationId.toUpperCase());
        String providerUserId = oAuth2UserInfo.getId();

        log.debug("Processing OAuth2 login - Provider: {}, Email: {}, ProviderUserId: {}",
                  provider, oAuth2UserInfo.getEmail(), providerUserId);

        // Find user by OAuth provider first
        Optional<User> userByOAuth = userRepository.findByOAuthProvider(provider, providerUserId);

        User user;
        if (userByOAuth.isPresent()) {
            // User found via OAuth provider
            user = userByOAuth.get();
            user.updateOAuthProviderUsage(provider);
            user.updateFromOAuthProfile(
                    oAuth2UserInfo.getName(),
                    oAuth2UserInfo.getFirstName(),
                    oAuth2UserInfo.getLastName(),
                    oAuth2UserInfo.getImageUrl()
                                       );
            log.info("User logged in via existing OAuth: {} - {}", provider, user.getEmail());
        } else {
            // Check if user exists by email
            Optional<User> userByEmail = userRepository.findByEmail(oAuth2UserInfo.getEmail());
            if (userByEmail.isPresent()) {
                // User exists by email - link this OAuth provider
                user = userByEmail.get();

                // Check if provider is already linked (by different providerUserId)
                if (user.hasOAuthProvider(provider)) {
                    log.warn("Provider {} already linked to user {}, updating providerUserId",
                             provider, user.getEmail());
                    // Update the existing provider with new providerUserId (in case it changed)
                    user.removeOAuthProvider(provider);
                }

                user.addOAuthProvider(provider, providerUserId);
                user.updateFromOAuthProfile(
                        oAuth2UserInfo.getName(),
                        oAuth2UserInfo.getFirstName(),
                        oAuth2UserInfo.getLastName(),
                        oAuth2UserInfo.getImageUrl()
                                           );
                log.info("Linked OAuth provider to existing user: {} - {}", provider, user.getEmail());
            } else {
                // Create new user with OAuth provider
                user = User.createFromOAuth(
                        oAuth2UserInfo.getEmail(),
                        oAuth2UserInfo.getName(),
                        oAuth2UserInfo.getFirstName(),
                        oAuth2UserInfo.getLastName(),
                        oAuth2UserInfo.getImageUrl(),
                        provider,
                        providerUserId
                                           );
                log.info("Created new user via OAuth: {} - {}", provider, user.getEmail());
            }
        }

        user = userRepository.save(user);
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }


}