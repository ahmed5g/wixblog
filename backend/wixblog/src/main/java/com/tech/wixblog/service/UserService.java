package com.tech.wixblog.service;

import com.tech.wixblog.dto.user.RegisterRequest;
import com.tech.wixblog.dto.user.UpdateUserRequest;
import com.tech.wixblog.dto.user.UserResponse;
import com.tech.wixblog.dto.user.UserStatsResponse;
import com.tech.wixblog.exception.OAuth2AuthenticationProcessingException;
import com.tech.wixblog.exception.UserAlreadyExistsException;
import com.tech.wixblog.exception.UserNotFoundException;
import com.tech.wixblog.mapper.UserMapper;
import com.tech.wixblog.model.User;
import com.tech.wixblog.model.enums.AuthProvider;
import com.tech.wixblog.model.enums.Role;
import com.tech.wixblog.repository.UserRepository;
import com.tech.wixblog.security.oauth2.user.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements OAuthUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;



    public UserResponse createUser (RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }
        User user = userMapper.createRequestToUser(request);
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        User savedUser = userRepository.save(user);
        return userMapper.userToUserResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getCurrentUserInfo (Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.userToUserResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserInfoById(Long id) {
        return getCurrentUserInfo(id);
    }



    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers () {
        return userMapper.usersToUsersResponse(userRepository.findAll());
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.userToUserResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Email: " + email));
        return userMapper.userToUserResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByRole(Role role) {
        return userMapper.usersToUsersResponse(userRepository.findByRole(role));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getActiveUsers () {
        return userMapper.usersToUsersResponse(userRepository.findByEnabledTrue());
    }

    @Transactional(readOnly = true)
    public List<UserResponse> searchUsers(String searchTerm) {
        return userMapper.usersToUsersResponse(
                userRepository.findByNameOrEmailContaining(searchTerm, searchTerm)
                                         );
    }


    public UserResponse updateUser (Long id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userMapper.updateUserFromRequest(request, user);
        User updatedUser = userRepository.save(user);

        return userMapper.userToUserResponse(updatedUser);
    }

    public UserResponse updateUserRole(Long id, Role role) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setRole(role);
        User updatedUser = userRepository.save(user);

        return userMapper.userToUserResponse(updatedUser);
    }




    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
    }

    // UTILITY METHODS
    @Transactional(readOnly = true)
    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public UserStatsResponse getUserStatistics () {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countActiveUsers();
        Map<Role, Long> usersByRole = userRepository.countUsersByRole();

        Map<Role, Long> filteredUsersByRole = usersByRole.entrySet().stream()
                .filter(entry -> entry.getKey() != null)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                                         ));

        return UserStatsResponse.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .inactiveUsers(totalUsers - activeUsers)
                .usersByRole(filteredUsersByRole)
                .build();
    }


    @Override
    public User processOAuthUser (AuthProvider provider, OAuth2UserInfo userInfo) {
        validateOAuthUserInfo(provider, userInfo);

        return findOrCreateUser(provider, userInfo);
    }

    @Override
    public User findOrCreateUser(AuthProvider provider, OAuth2UserInfo userInfo) {
        Optional<User> userByOAuth = findUserByOAuthProviderOptional(provider, userInfo.getId());
        if (userByOAuth.isPresent()) {
            return handleExistingOAuthUser(userByOAuth.get(), provider, userInfo);
        }

        Optional<User> userByEmail = findUserByEmailOptional(userInfo.getEmail());
        if (userByEmail.isPresent()) {
            return linkOAuthToExistingUser(userByEmail.get(), provider, userInfo);
        }

        return createNewOAuthUser(provider, userInfo);
    }

    @Override
    public User findUserByOAuthProvider(AuthProvider provider, String providerUserId) {
        return userRepository.findByOAuthProvider(provider, providerUserId)
                .orElse(null);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }

    @Override
    public User linkOAuthToExistingUser(User user, AuthProvider provider, OAuth2UserInfo userInfo) {

        handleProviderConflict(user, provider);
        user.addOAuthProvider(provider, userInfo.getId());
        updateUserProfile(user, userInfo);

        return userRepository.save(user);
    }

    @Override
    public User createNewOAuthUser(AuthProvider provider, OAuth2UserInfo userInfo) {

        User user = User.createFromOAuth(
                userInfo.getEmail(),
                userInfo.getName(),
                userInfo.getFirstName(),
                userInfo.getLastName(),
                userInfo.getImageUrl(),
                provider,
                userInfo.getId()
                                        );

        return userRepository.save(user);
    }

    @Override
    public void updateUserProfile (User user, OAuth2UserInfo userInfo) {
        user.updateFromOAuthProfile(
                userInfo.getName(),
                userInfo.getFirstName(),
                userInfo.getLastName(),
                userInfo.getImageUrl()
                                   );
        user.recordLogin();
    }

    // Private helper methods
    private void validateOAuthUserInfo(AuthProvider provider, OAuth2UserInfo userInfo) {
        if (StringUtils.isEmpty(userInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
    }

    private Optional<User> findUserByOAuthProviderOptional(AuthProvider provider, String providerUserId) {
        return userRepository.findByOAuthProvider(provider, providerUserId);
    }

    private Optional<User> findUserByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }

    private User handleExistingOAuthUser(User user, AuthProvider provider, OAuth2UserInfo userInfo) {
        user.updateOAuthProviderUsage(provider);
        updateUserProfile(user, userInfo);
        return userRepository.save(user);
    }

    private void handleProviderConflict(User user, AuthProvider provider) {
        if (user.hasOAuthProvider(provider)) {
            user.removeOAuthProvider(provider);
        }
    }
}