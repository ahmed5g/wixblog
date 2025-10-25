//package com.tech.wixblog.models;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.Objects;
//
//
//@Entity
//@Table(name = "users")
//@RequiredArgsConstructor
//@NoArgsConstructor
//@AllArgsConstructor
//public class Upser {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(unique = true, nullable = false)
//    private String email;
//
//    private String name;
//    private String pictureUrl;
//
//    @Enumerated(EnumType.STRING)
//    private Role role;
//
//    private String password;
//
//    private String providerId;
//
//    @Enumerated(EnumType.STRING)
//    private AuthProvider provider;
//
//    private boolean emailVerified;
//    private boolean active = true;
//
//    public String getEmail () {
//        return email;
//    }
//
//    public void setEmail (String email) {
//        this.email = email;
//    }
//
//    public String getName () {
//        return name;
//    }
//
//    public void setName (String name) {
//        this.name = name;
//    }
//
//    public String getPictureUrl () {
//        return pictureUrl;
//    }
//
//    public void setPictureUrl (String pictureUrl) {
//        this.pictureUrl = pictureUrl;
//    }
//
//    public Role getRole () {
//        return role;
//    }
//
//    public void setRole (Role role) {
//        this.role = role;
//    }
//
//    public String getPassword () {
//        return password;
//    }
//
//    public void setPassword (String password) {
//        this.password = password;
//    }
//
//    public String getProviderId () {
//        return providerId;
//    }
//
//    public void setProviderId (String providerId) {
//        this.providerId = providerId;
//    }
//
//    public AuthProvider getProvider () {
//        return provider;
//    }
//
//    public void setProvider (AuthProvider provider) {
//        this.provider = provider;
//    }
//
//    public boolean isEmailVerified () {
//        return emailVerified;
//    }
//
//    public void setEmailVerified (boolean emailVerified) {
//        this.emailVerified = emailVerified;
//    }
//
//    public boolean isActive () {
//        return active;
//    }
//
//    public void setActive (boolean active) {
//        this.active = active;
//    }
//
//    @Override
//    public boolean equals (Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Upser user = (Upser) o;
//        return emailVerified == user.emailVerified && active == user.active && Objects.equals(email, user.email) && Objects.equals(name, user.name) && Objects.equals(pictureUrl, user.pictureUrl) && role == user.role && Objects.equals(password, user.password) && Objects.equals(providerId, user.providerId) && provider == user.provider;
//    }
//
//    @Override
//    public int hashCode () {
//        return Objects.hash(email, name, pictureUrl, role, password, providerId, provider, emailVerified, active);
//    }
//}
