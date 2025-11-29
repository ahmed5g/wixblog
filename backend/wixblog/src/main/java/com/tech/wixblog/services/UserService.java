package com.tech.wixblog.services;

import com.tech.wixblog.dto.payload.UserStatsResponse;
import com.tech.wixblog.dto.payload.RegisterRequest;
import com.tech.wixblog.dto.payload.UpdateUserRequest;
import com.tech.wixblog.dto.payload.UserResponse;
import com.tech.wixblog.exception.UserAlreadyExistsException;
import com.tech.wixblog.exception.UserNotFoundException;
import com.tech.wixblog.mapper.UserMapper;
import com.tech.wixblog.model.Role;
import com.tech.wixblog.model.User;
import com.tech.wixblog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService  {
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
}