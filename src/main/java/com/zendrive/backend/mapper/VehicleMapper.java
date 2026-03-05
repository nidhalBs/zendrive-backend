package com.zendrive.backend.mapper;

import com.zendrive.backend.dto.CreateVehicleRequest;
import com.zendrive.backend.dto.VehicleDTO;
import com.zendrive.backend.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    VehicleDTO toDTO(Vehicle vehicle);
    Vehicle toEntity(CreateVehicleRequest request);
    void updateEntity(CreateVehicleRequest request, @MappingTarget Vehicle vehicle);
}
