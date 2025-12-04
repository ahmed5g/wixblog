package com.tech.wixblog.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthProviderInfoDto {
    private String providerUserId;
    private LocalDateTime linkedAt;
    private LocalDateTime lastUsedAt;
}