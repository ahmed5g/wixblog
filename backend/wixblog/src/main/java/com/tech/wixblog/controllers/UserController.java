package com.tech.wixblog.controllers;

import com.tech.wixblog.dto.payload.UserResponse;
import com.tech.wixblog.mapper.UserMapper;
import com.tech.wixblog.security.CurrentUser;
import com.tech.wixblog.security.UserPrincipal;
import com.tech.wixblog.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
//    @GetMapping("/my-profile")
//    public ResponseEntity<UserDTO> getMyProfile (@AuthenticationPrincipal OAuth2User principal) {
//        User currentUser = userService.extractUserFromOAuth2User(principal);
//        Optional<UserDTO> userProfile = userService.getUserProfile(currentUser.getId(), currentUser);
//        return userProfile.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/profile")
//    public ResponseEntity<UserDTO> updateMyProfile (
//            @Valid @RequestBody UpdateProfileDTO updateProfileDTO,
//            @AuthenticationPrincipal OAuth2User principal) {
//        User currentUser = userService.extractUserFromOAuth2User(principal);
//        Optional<UserDTO> updatedProfile = userService.updateUserProfile(
//                //
//                currentUser.getId(), updateProfileDTO, currentUser);
//        return updatedProfile.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/settings")
//    public ResponseEntity<UserDTO> updateMySettings (
//            @Valid @RequestBody UpdateSettingsDTO updateSettingsDTO,
//            @AuthenticationPrincipal OAuth2User principal) {
//        User currentUser = userService.extractUserFromOAuth2User(principal);
//        Optional<UserDTO> updatedSettings = userService.updateUserSettings(
//                currentUser.getId(), updateSettingsDTO, currentUser);
//        return updatedSettings.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<Page<UserDTO>> searchUsers (
//            @RequestParam String query,
//            @ParameterObject Pageable pageable) {
//        Page<UserDTO> users = userService.searchUsers(query, pageable);
//        return ResponseEntity.ok(users);
//    }
//
//    @GetMapping("/top-writers")
//    public ResponseEntity<Page<UserDTO>> getTopWriters (
//            @ParameterObject Pageable pageable) {
//        Page<UserDTO> users = userService.getTopWriters(pageable);
//        return ResponseEntity.ok(users);
//    }
//
//
//
//    @PostMapping("/update-user-stats")
//    public ResponseEntity<UserDTO> getUpdatedStats (@AuthenticationPrincipal OAuth2User principal,
//                                                    @RequestBody @Valid UpdateProfileStatsDTO updateProfileStats) {
//        User currentUser = userService.extractUserFromOAuth2User(principal);
//        userService.updateUserStats(currentUser, updateProfileStats);
//        return ResponseEntity.ok(userMapper.userToUserDTO(currentUser));
//    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser (@CurrentUser
            UserPrincipal userPrincipal) {
        return ResponseEntity.ok(userService.getUserInfoById(userPrincipal.getId()));
    }


}