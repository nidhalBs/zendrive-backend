package com.zendrive.backend.mapper;

import com.zendrive.backend.dto.RouteDTO;
import com.zendrive.backend.entity.Route;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RouteMapper {
    RouteDTO toDTO(Route route);
    Route toEntity(RouteDTO dto);
}
