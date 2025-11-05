package com.tech.wixblog.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.tech.wixblog.dto.AuthResponseDTO;
import com.tech.wixblog.mapper.UserMapper;
import com.tech.wixblog.models.User;
import com.tech.wixblog.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "1. Authentication", description = "Authentication endpoints")
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;

//    @Value("${spring.security.oauth2.client.registration.google.client-id}")
//    private String googleClientId;
//

    @Operation(summary = "Get current user",
               description = "Returns the currently authenticated user details")
    @GetMapping("/user")
    public ResponseEntity<AuthResponseDTO> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User currentUser = userService.extractUserFromOAuth2User(principal);
        User user = userService.findOrCreateUser(
                currentUser.getEmail(),
                currentUser.getName(),
                currentUser.getProfilePicture()
                                                );
        AuthResponseDTO response = userMapper.userToAuthResponseDTO(user);
        return ResponseEntity.ok(response);
    }


//    @PostMapping("/google-login")
//    public ResponseEntity<AuthResponseDTO> googleLogin (@RequestBody Map<String, String> payload) {
//        String token = payload.get("token");
//        if (token == null) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        try {
//            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
//                    new NetHttpTransport(),
//                    JacksonFactory.getDefaultInstance()
//            ).setAudience(
//                            Collections.singletonList(googleClientId))
//                    .build();
//
//            GoogleIdToken idToken = verifier.verify(token);
//            if (idToken == null) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//            }
//
//            GoogleIdToken.Payload googlePayload = idToken.getPayload();
//            String email = googlePayload.getEmail();
//            String name = (String) googlePayload.get("name");
//            String picture = (String) googlePayload.get("picture");
//
//            User user = userService.findOrCreateUser(email, name, picture);
//            AuthResponseDTO response = userMapper.userToAuthResponseDTO(user);
//
//            // Optionally: Create session or cookie
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }



    @PostMapping("/logout")
    public ResponseEntity<Void> logout (
            HttpServletRequest request,
            HttpServletResponse response,
            @AuthenticationPrincipal OAuth2User principal) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}