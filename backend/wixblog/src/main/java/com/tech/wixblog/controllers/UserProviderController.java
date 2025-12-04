package com.tech.wixblog.controllers;

import com.tech.wixblog.model.User;
import com.tech.wixblog.model.enums.AuthProvider;
import com.tech.wixblog.repository.UserRepository;
import com.tech.wixblog.security.CurrentUser;
import com.tech.wixblog.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("user/providers")
@RequiredArgsConstructor
public class UserProviderController {
    
    private final UserRepository userRepository;
    

    
    @DeleteMapping("/{provider}")
    public ResponseEntity<?> unlinkProvider (@CurrentUser UserPrincipal userPrincipal,
                                             @PathVariable AuthProvider provider) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        try {
            user.removeOAuthProvider(provider);
            userRepository.save(user);
            return ResponseEntity.ok().body(Map.of("message", "Provider unlinked successfully"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    

}