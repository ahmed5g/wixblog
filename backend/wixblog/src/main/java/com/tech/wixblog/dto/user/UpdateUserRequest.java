package com.tech.wixblog.dto.user;

import com.tech.wixblog.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest  {
    
    private String name;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String bio;
    private Role role;
    private Boolean enabled;
    

    
    // Settings
    private Boolean emailNotifications;
    private Boolean commentNotifications;
    private Boolean likeNotifications;
    private Boolean newsletterSubscribed;
    private Boolean showOnlineStatus;
}