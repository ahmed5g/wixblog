package com.tech.wixblog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private Long id;
    private String email;
    private String name;
    private String profilePicture;
    private String role;
    private String createdAt;
    private boolean authenticated;

}