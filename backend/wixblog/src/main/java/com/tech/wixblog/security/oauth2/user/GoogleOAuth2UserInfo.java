package com.tech.wixblog.security.oauth2.user;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
    public GoogleOAuth2UserInfo (Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId () {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName () {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail () {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        String picture = (String) attributes.get("picture");
        System.out.println("DEBUG: GoogleOAuth2UserInfo.getImageUrl()");
        System.out.println("  Attributes keys: " + attributes.keySet());
        System.out.println("  Picture attribute value: " + picture);
        System.out.println("  Picture attribute type: " + (picture != null ? picture.getClass() : "null"));
        return picture;
    }

    @Override
    public String getFirstName () {return (String) attributes.get("given_name");}

    @Override
    public String getLastName () {return (String) attributes.get("family_name");}
}