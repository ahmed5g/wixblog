package com.tech.wixblog.controllers;

import com.tech.wixblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;


//    @GetMapping("/users")
//    public ResponseEntity<List<UserDTO>> getAllUsers () {
//        List<UserDTO> usersDTOs = userService.findAllUsers();
//        return ResponseEntity.ok(usersDTOs);
//    }
//
//    @PutMapping("/users/promote")
//    public ResponseEntity<UserDTO> updateUserRole (
//            @AuthenticationPrincipal OAuth2User connectedUser,
//            @Parameter(description = "select a role", required = true, schema =
//            @Schema(implementation = Role.class)) Role userRole) {
//        User currentUser = userService.extractUserFromOAuth2User(connectedUser);
//        UserDTO userDTO = userService.updateUserRole(currentUser, userRole);
//        return ResponseEntity.ok(userDTO);
//    }


}