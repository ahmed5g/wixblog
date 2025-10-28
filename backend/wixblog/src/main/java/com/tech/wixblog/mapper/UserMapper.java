package com.tech.wixblog.mapper;

import com.tech.wixblog.dto.AuthResponseDTO;
import com.tech.wixblog.dto.UserDTO;
import com.tech.wixblog.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "profilePicture", target = "profilePicture")
    UserDTO userToUserDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "profilePicture", target = "profilePicture")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(expression = "java(true)", target = "authenticated")
    AuthResponseDTO userToAuthResponseDTO(User user);

    default AuthResponseDTO toAuthResponseDTO(User user, boolean authenticated) {
        if (user == null) {
            return AuthResponseDTO.builder()
                    .authenticated(false)
                    .build();
        }

        return AuthResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .profilePicture(user.getProfilePicture())
                .role(user.getRole().name())
                .authenticated(authenticated)
                .build();
    }
}