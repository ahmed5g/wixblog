package com.tech.wixblog.controllers;

import com.tech.wixblog.dto.AuthResponseDTO;
import com.tech.wixblog.mapper.UserMapper;
import com.tech.wixblog.models.User;
import com.tech.wixblog.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/user")
    public ResponseEntity<AuthResponseDTO> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            log.debug("No authenticated user found");
            return ResponseEntity.ok(
                    AuthResponseDTO.builder()
                            .authenticated(false)
                            .build()
            );
        }

        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        String profilePicture = principal.getAttribute("picture");

        log.info("Processing OAuth2 login for user: {}", email);

        User user = userService.findOrCreateUser(email, name, profilePicture);

        // Use MapStruct mapper instead of manual conversion
        AuthResponseDTO response = userMapper.userToAuthResponseDTO(user);

        log.debug("Returning auth response for user: {}", user.getEmail());
        return ResponseEntity.ok(response);
    }
}