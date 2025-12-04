package com.tech.wixblog.mapper;

import com.tech.wixblog.dto.user.OAuthProviderInfoDto;
import com.tech.wixblog.model.OAuthProviderInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OAuthProviderInfoMapper {
    
    OAuthProviderInfoDto toDto(OAuthProviderInfo entity);
    
    OAuthProviderInfo toEntity(OAuthProviderInfoDto dto);
}