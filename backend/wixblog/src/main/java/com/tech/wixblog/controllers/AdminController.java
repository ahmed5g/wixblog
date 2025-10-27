package com.tech.wixblog.controllers;

import com.tech.wixblog.dto.UserDTO;
import com.tech.wixblog.mapper.UserListMapper;
import com.tech.wixblog.mapper.UserMapper;
import com.tech.wixblog.models.Role;
import com.tech.wixblog.models.User;
import com.tech.wixblog.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserListMapper userListMapper;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.findAllUsers();

        // Use MapStruct for list conversion
        List<UserDTO> userDTOs = userListMapper.usersToUserDTOs(users);

        log.debug("Returning {} users for admin", users.size());
        return ResponseEntity.ok(userDTOs);
    }

    @PutMapping("/users/{userId}/promote-to-admin")
    public ResponseEntity<UserDTO> promoteToAdmin(@PathVariable Long userId) {
        User user = userService.updateUserRole(userId, Role.ROLE_ADMIN);

        // Use MapStruct mapper
        UserDTO userDTO = userMapper.userToUserDTO(user);

        log.info("Promoted user {} to ADMIN", user.getEmail());
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/users/{userId}/demote-to-user")
    public ResponseEntity<UserDTO> demoteToUser(@PathVariable Long userId) {
        User user = userService.updateUserRole(userId, Role.ROLE_USER);

        // Use MapStruct mapper
        UserDTO userDTO = userMapper.userToUserDTO(user);

        log.info("Demoted user {} to USER", user.getEmail());
        return ResponseEntity.ok(userDTO);
    }
}