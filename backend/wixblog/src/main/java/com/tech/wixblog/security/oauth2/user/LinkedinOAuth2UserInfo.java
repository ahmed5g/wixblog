package com.tech.wixblog.security.oauth2.user;

import java.util.Map;

public class LinkedinOAuth2UserInfo extends OAuth2UserInfo {

    public LinkedinOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        String firstName = getFirstName();
        String lastName = getLastName();
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return (String) attributes.get("name");
    }
    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        if (attributes.containsKey("picture")) {
            return (String) attributes.get("picture");
        }
        return null;
    }
    @Override
    public String getFirstName() {
        return (String) attributes.get("given_name");
    }

    @Override
    public String getLastName() {
        return (String) attributes.get("family_name");
    }
}