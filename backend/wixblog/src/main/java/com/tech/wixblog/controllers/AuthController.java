package com.tech.wixblog.controllers;

import com.tech.wixblog.services.GoogleAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final GoogleAuthService googleAuthService;

    public AuthController(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @PostMapping("/google")
    public Map<String, Object> login(@RequestBody Map<String, String> body) throws Exception {
        String token = body.get("token");
        return googleAuthService.verifyToken(token);
    }
}
