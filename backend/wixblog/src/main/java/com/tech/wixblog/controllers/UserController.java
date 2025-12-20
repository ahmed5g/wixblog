package com.tech.wixblog.controllers;

import com.tech.wixblog.dto.user.*;
import com.tech.wixblog.model.enums.Role;
import com.tech.wixblog.security.CurrentUser;
import com.tech.wixblog.security.UserPrincipal;
import com.tech.wixblog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser (
            @Valid @RequestBody RegisterRequest request,
            @CurrentUser UserPrincipal currentUser) {
        if (currentUser != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error", "already_authenticated",
                            "message",
                            "Cannot register new account while already logged in as: " + currentUser.getEmail()
                                ));
        }
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser (@CurrentUser UserPrincipal userPrincipal) {
        UserResponse userResponse = userService.getCurrentUserInfo(userPrincipal.getId());
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers () {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById (@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUserByEmail (@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponse>> getUsersByRole (@PathVariable Role role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    //todo change to get connected users
    @GetMapping("/active")
    public ResponseEntity<List<UserResponse>> getActiveUsers () {
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers (@RequestParam String q) {
        return ResponseEntity.ok(userService.searchUsers(q));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser (
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponse> updateUserRole (
            @PathVariable Long id,
            @RequestParam Role role) {
        return ResponseEntity.ok(userService.updateUserRole(id, role));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User has been deleted successfully");
    }


     @GetMapping("/statistics")
    public ResponseEntity<UserStatsResponse> getUserStatistics () {
        return ResponseEntity.ok(userService.getUserStatistics());
    }

}