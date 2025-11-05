package com.tech.wixblog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Update Profile DTO
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileDTO {
    private String name;
    private String bio;
    private String websiteUrl;
    private String twitterHandle;
    private String githubUsername;
    private String linkedinUrl;
    private String location;
    private String company;
    private String jobTitle;
}
