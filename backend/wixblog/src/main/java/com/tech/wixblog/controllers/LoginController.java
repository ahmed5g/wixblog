package com.tech.wixblog.controllers;

import com.tech.wixblog.dto.payload.LoginRequest;
import com.tech.wixblog.security.CurrentUser;
import com.tech.wixblog.security.UserPrincipal;
import com.tech.wixblog.services.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest,
                                   @CurrentUser UserPrincipal currentUser) {
        if (currentUser != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error", "already_authenticated",
                            "message", "Cannot login while already authenticated as: " + currentUser.getEmail()
                                ));
        }
        return ResponseEntity.ok(loginService.login(loginRequest));
    }
}