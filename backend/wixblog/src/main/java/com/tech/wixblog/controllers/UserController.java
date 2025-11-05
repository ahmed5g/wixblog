package com.tech.wixblog.controllers;

import com.tech.wixblog.dto.UserDTO;
import com.tech.wixblog.mapper.UserMapper;
import com.tech.wixblog.models.User;
import com.tech.wixblog.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAnyRole('ADMIN','USER')") // Changed to hasAnyAuthority
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Use MapStruct mapper instead of manual conversion
        UserDTO userDTO = userMapper.userToUserDTO(user);

        log.debug("Returning profile for user: {}", email);
        return ResponseEntity.ok(userDTO);
    }
}