package com.tech.wixblog.model.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum AuthProvider {
    LOCAL("local"),
    GOOGLE("google"),
    GITHUB("github"),
    FACEBOOK("facebook"),
    LINKEDIN("linkedin"),
    TWITTER("twitter"),
    MICROSOFT("microsoft");

    private final String registrationId;

    AuthProvider(String registrationId) {
        this.registrationId = registrationId;
    }

    public static AuthProvider fromRegistrationId(String registrationId) {
        if (registrationId == null) {
            return null;
        }

        for (AuthProvider provider : AuthProvider.values()) {
            if (provider.registrationId.equalsIgnoreCase(registrationId)) {
                return provider;
            }
        }
        return null;
    }
}