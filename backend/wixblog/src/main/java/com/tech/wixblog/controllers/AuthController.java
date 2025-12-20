package com.tech.wixblog.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "1. Authentication", description = "Authentication endpoints")
public class AuthController {
    private final com.tech.wixblog.config.AppProperties appProperties;

    @Operation(
            summary = "Logout user",
            description = "Clears client-side authentication. Note: Short-lived tokens (30 min) will expire automatically."
    )
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        clearAuthCookies(response);

        // Calculate expiration time from properties
        long expirationMinutes = appProperties.getAuth().getTokenExpirationMsec() / 60000L;

        return ResponseEntity.ok(Map.of(
                "message", "Logged out successfully",
                "note", "Your session token will expire in " + expirationMinutes + " minutes",
                "action", "Please discard your token client-side",
                "token_lifetime_minutes", String.valueOf(expirationMinutes)
                                       ));
    }

    @Operation(summary = "Refresh token", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION)
            Authentication authentication,
            com.tech.wixblog.security.TokenProvider tokenProvider) {

        String newToken = tokenProvider.createToken(authentication);

        long expiresInSeconds = appProperties.getAuth().getTokenExpirationMsec() / 1000L;

        return ResponseEntity.ok(Map.of(
                "token", newToken,
                "token_type", "Bearer",
                "expires_in", String.valueOf(expiresInSeconds),
                "message", "Token refreshed successfully"
                                       ));
    }

    private void clearAuthCookies(HttpServletResponse response) {
        Cookie sessionCookie = new Cookie("JSESSIONID", "");
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(0);
        response.addCookie(sessionCookie);

        Cookie authCookie = new Cookie("auth_token", "");
        authCookie.setPath("/");
        authCookie.setMaxAge(0);
        authCookie.setHttpOnly(true);
        response.addCookie(authCookie);

        Cookie oauthCookie = new Cookie("oauth2_auth_request", "");
        oauthCookie.setPath("/");
        oauthCookie.setMaxAge(0);
        response.addCookie(oauthCookie);
    }

    @Operation(summary = "Get token configuration")
    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> getTokenConfig() {
        return ResponseEntity.ok(Map.of(
                "token_lifetime_minutes", appProperties.getAuth().getTokenExpirationMsec() / 60000L,
                "remember_me_days", appProperties.getAuth().getRememberMeExpirationMsec() / 86400000L,
                "logout_behavior", "Client-side token discard (tokens expire in " +
                        (appProperties.getAuth().getTokenExpirationMsec() / 60000L) + " minutes)"
                                       ));
    }
}