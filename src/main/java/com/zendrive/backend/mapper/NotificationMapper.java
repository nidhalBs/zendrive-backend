package com.zendrive.backend.mapper;

import com.zendrive.backend.dto.NotificationDTO;
import com.zendrive.backend.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "userId", source = "user.id")
    NotificationDTO toDTO(Notification notification);
}
