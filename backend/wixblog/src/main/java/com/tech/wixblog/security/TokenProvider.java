package com.tech.wixblog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tech.wixblog.config.AppProperties;
import com.tech.wixblog.model.User;
import com.tech.wixblog.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {
    private final AppProperties appProperties;
    private final UserRepository userRepository;
    private JWTVerifier verifier;
    private Algorithm ALGORITHM;

    @PostConstruct
    public void init () {
        this.ALGORITHM = Algorithm.HMAC256(appProperties.getAuth().getTokenSecret());
        this.verifier = JWT.require(ALGORITHM).build();
    }

    public String createToken (Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user;
        ALGORITHM = Algorithm.HMAC256(appProperties.getAuth().getTokenSecret());
        Date now = new Date();
        Date expirationDate = new Date(
                now.getTime() + appProperties.getAuth().getTokenExpirationMsec());
        user = userRepository.findByEmail(userPrincipal.getEmail())
                .orElseThrow(() -> new RuntimeException(
                        "User not found with email: " + userPrincipal.getEmail()));
        String profilePicture = user.getProfilePicture();

        return JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("email", user.getEmail())
                .withClaim("name", user.getName())
                .withClaim("role", user.getRole().name())
                .withClaim("picture", profilePicture)
                .withClaim("first_name", user.getFirstName())
                .withClaim("last_name", user.getLastName())
                .withClaim("created_at", user.getCreatedAt() != null ?
                        user.getCreatedAt().toEpochSecond(java.time.ZoneOffset.UTC) :
                        now.getTime() / 1000)
                .withIssuedAt(now)
                .withExpiresAt(expirationDate)
                .withIssuer("wixblog")
                .sign(ALGORITHM);
    }

    public String createTokenFromUserCredentials (User user) {


        ALGORITHM = Algorithm.HMAC256(appProperties.getAuth().getTokenSecret());
        Date now = new Date();
        Date expirationDate = new Date(
                now.getTime() + appProperties.getAuth().getTokenExpirationMsec());
        User finalUser = user;
        user = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException(
                        "User not found with email: " + finalUser.getEmail()));
        String profilePicture = user.getProfilePicture();

        return JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("email", user.getEmail())
                .withClaim("name", user.getName())
                .withClaim("role", user.getRole().name())
                .withClaim("picture", profilePicture)
                .withClaim("first_name", user.getFirstName())
                .withClaim("last_name", user.getLastName())
                .withClaim("created_at", user.getCreatedAt() != null ?
                        user.getCreatedAt().toEpochSecond(java.time.ZoneOffset.UTC) :
                        now.getTime() / 1000)
                .withIssuedAt(now)
                .withExpiresAt(expirationDate)
                .withIssuer("wixblog")
                .sign(ALGORITHM);
    }

    public Long getUserIdFromToken (String token) {
        JWTVerifier verifier = JWT.require(ALGORITHM).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String subject = decodedJWT.getSubject();
        return Long.parseLong(subject);
    }

    public boolean validateToken (String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid or expired JWT.");
        }
        return false;
    }

}