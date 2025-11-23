package com.tech.wixblog.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;
import java.util.Optional;

public class CookieUtils {

    /**
     * Get cookie by name from HttpServletRequest
     */
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }
        
        return Optional.empty();
    }

    /**
     * Get cookie value by name from HttpServletRequest
     */
    public static Optional<String> getCookieValue(HttpServletRequest request, String name) {
        return getCookie(request, name).map(Cookie::getValue);
    }

    /**
     * Add cookie to HttpServletResponse
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * Add cookie with additional security options
     */
    public static void addSecureCookie(HttpServletResponse response, String name, String value, int maxAge, 
                                      boolean secure, String sameSite) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(secure);
        
        // For SameSite attribute (requires manual header manipulation in some servlet containers)
        if (sameSite != null) {
            String cookieHeader = String.format("%s=%s; Path=%s; HttpOnly; Max-Age=%d; %sSameSite=%s",
                    name, value, "/", maxAge, secure ? "Secure; " : "", sameSite);
            response.addHeader("Set-Cookie", cookieHeader);
        } else {
            response.addCookie(cookie);
        }
    }

    /**
     * Delete cookie by name
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    /**
     * Serialize an object to base64 string for cookie storage
     */
    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    /**
     * Deserialize a base64 string to object from cookie value
     */
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.getValue())));
    }

    /**
     * Deserialize a base64 string to object
     */
    public static <T> T deserialize(String value, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(value)));
    }

    /**
     * Check if a cookie exists in the request
     */
    public static boolean hasCookie(HttpServletRequest request, String name) {
        return getCookie(request, name).isPresent();
    }

    /**
     * Get all cookies as a formatted string for debugging
     */
    public static String getCookiesAsString(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return "No cookies";
        }
        
        StringBuilder sb = new StringBuilder();
        for (Cookie cookie : cookies) {
            sb.append(String.format("[Name: %s, Value: %s, Domain: %s, Path: %s, MaxAge: %d]",
                    cookie.getName(), 
                    cookie.getValue(), 
                    cookie.getDomain() != null ? cookie.getDomain() : "default",
                    cookie.getPath() != null ? cookie.getPath() : "default",
                    cookie.getMaxAge()));
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Create a cookie with common OAuth2 settings
     */
    public static Cookie createOAuth2Cookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(true); // Should be true in production with HTTPS
        return cookie;
    }

    /**
     * Add multiple cookies at once
     */
    public static void addCookies(HttpServletResponse response, Cookie... cookies) {
        for (Cookie cookie : cookies) {
            response.addCookie(cookie);
        }
    }
}