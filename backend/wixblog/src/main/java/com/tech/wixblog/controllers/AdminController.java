package com.tech.wixblog.controllers;

import com.tech.wixblog.dto.UserDTO;
import com.tech.wixblog.mapper.UserListMapper;
import com.tech.wixblog.mapper.UserMapper;
import com.tech.wixblog.models.Role;
import com.tech.wixblog.models.User;
import com.tech.wixblog.services.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;


    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers () {
        List<UserDTO> usersDTOs = userService.findAllUsers();
        return ResponseEntity.ok(usersDTOs);
    }

    @PutMapping("/users/promote")
    public ResponseEntity<UserDTO> updateUserRole (
            @AuthenticationPrincipal OAuth2User connectedUser,
            @Parameter(description = "select a role", required = true, schema =
            @Schema(implementation = Role.class)) Role userRole) {
        User currentUser = userService.extractUserFromOAuth2User(connectedUser);
        UserDTO userDTO = userService.updateUserRole(currentUser, userRole);
        return ResponseEntity.ok(userDTO);
    }


}