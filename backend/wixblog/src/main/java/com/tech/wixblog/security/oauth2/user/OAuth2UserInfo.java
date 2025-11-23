package com.tech.wixblog.security.oauth2.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;
    
    public abstract String getId();
    
    public abstract String getName();
    
    public abstract String getEmail();
    
    public abstract String getImageUrl();

    public abstract String getFirstName();
    public abstract String getLastName();
    
}