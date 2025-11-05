package com.tech.wixblog.controllers;

import com.tech.wixblog.dto.UpdateProfileDTO;
import com.tech.wixblog.dto.UpdateProfileStatsDTO;
import com.tech.wixblog.dto.UpdateSettingsDTO;
import com.tech.wixblog.dto.UserDTO;
import com.tech.wixblog.mapper.UserMapper;
import com.tech.wixblog.models.User;
import com.tech.wixblog.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/my-profile")
    public ResponseEntity<UserDTO> getMyProfile (@AuthenticationPrincipal OAuth2User principal) {
        User currentUser = userService.extractUserFromOAuth2User(principal);
        Optional<UserDTO> userProfile = userService.getUserProfile(currentUser.getId(), currentUser);
        return userProfile.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateMyProfile (
            @Valid @RequestBody UpdateProfileDTO updateProfileDTO,
            @AuthenticationPrincipal OAuth2User principal) {
        User currentUser = userService.extractUserFromOAuth2User(principal);
        Optional<UserDTO> updatedProfile = userService.updateUserProfile(
                //
                currentUser.getId(), updateProfileDTO, currentUser);
        return updatedProfile.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/settings")
    public ResponseEntity<UserDTO> updateMySettings (
            @Valid @RequestBody UpdateSettingsDTO updateSettingsDTO,
            @AuthenticationPrincipal OAuth2User principal) {
        User currentUser = userService.extractUserFromOAuth2User(principal);
        Optional<UserDTO> updatedSettings = userService.updateUserSettings(
                currentUser.getId(), updateSettingsDTO, currentUser);
        return updatedSettings.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserDTO>> searchUsers (
            @RequestParam String query,
            @ParameterObject Pageable pageable) {
        Page<UserDTO> users = userService.searchUsers(query, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/top-writers")
    public ResponseEntity<Page<UserDTO>> getTopWriters (
            @ParameterObject Pageable pageable) {
        Page<UserDTO> users = userService.getTopWriters(pageable);
        return ResponseEntity.ok(users);
    }



    @PostMapping("/update-user-stats")
    public ResponseEntity<UserDTO> getUpdatedStats (@AuthenticationPrincipal OAuth2User principal,
                                                    @RequestBody @Valid UpdateProfileStatsDTO updateProfileStats) {
        User currentUser = userService.extractUserFromOAuth2User(principal);
        userService.updateUserStats(currentUser, updateProfileStats);
        return ResponseEntity.ok(userMapper.userToUserDTO(currentUser));
    }
}