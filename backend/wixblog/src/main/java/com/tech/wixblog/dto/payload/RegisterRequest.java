package com.tech.wixblog.dto.payload;

import com.tech.wixblog.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest  {


    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Name is required")
    private String name;

    private String firstName;
    private String lastName;
    private String password;
    private String profilePicture;
    private String bio;
    private Role role;

    // Profile fields
    private String websiteUrl;
    private String location;
    private String company;
    private String jobTitle;
}