package com.tech.wixblog.mapper;

import com.tech.wixblog.dto.UserDTO;
import com.tech.wixblog.model.User;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserListMapper {

    List<UserDTO> usersToUserDTOs(List<User> users);
    List<User> userDTOsToUsers(List<UserDTO> userDTOs);
}
