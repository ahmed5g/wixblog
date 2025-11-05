package com.tech.wixblog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Update Settings DTO
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSettingsDTO {
    private Boolean emailNotifications;
    private Boolean commentNotifications;
    private Boolean likeNotifications;
    private Boolean newsletterSubscribed;
    private Boolean publicProfile;
    private Boolean showOnlineStatus;
}
