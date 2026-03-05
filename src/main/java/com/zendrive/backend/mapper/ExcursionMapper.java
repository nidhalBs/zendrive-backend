package com.zendrive.backend.mapper;

import com.zendrive.backend.dto.CreateExcursionRequest;
import com.zendrive.backend.dto.ExcursionDTO;
import com.zendrive.backend.dto.ExcursionStepDTO;
import com.zendrive.backend.entity.Excursion;
import com.zendrive.backend.entity.ExcursionStep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ExcursionMapper {
    ExcursionDTO toDTO(Excursion excursion);
    ExcursionStepDTO toStepDTO(ExcursionStep step);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "steps", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    Excursion toEntity(CreateExcursionRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "steps", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntity(CreateExcursionRequest request, @MappingTarget Excursion excursion);
}
