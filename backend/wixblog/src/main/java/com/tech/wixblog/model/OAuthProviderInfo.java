package com.tech.wixblog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OAuthProviderInfo {


    @Column(name = "provider_user_id")
    private String providerUserId;

    @Column(name = "linked_at")
    private LocalDateTime linkedAt;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    public static OAuthProviderInfo create(String providerUserId) {
        LocalDateTime now = LocalDateTime.now();
        return new OAuthProviderInfo(providerUserId, now, now);
    }

    public void updateLastUsed() {
        this.lastUsedAt = LocalDateTime.now();
    }
}