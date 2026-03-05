package com.zendrive.backend.mapper;

import com.zendrive.backend.dto.UserDTO;
import com.zendrive.backend.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
}
