package com.zendrive.backend.mapper;

import com.zendrive.backend.dto.ReservationDTO;
import com.zendrive.backend.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "clientName", expression = "java(reservation.getClient().getFirstName() + \" \" + reservation.getClient().getLastName())")
    @Mapping(target = "clientEmail", source = "client.email")
    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "vehicleName", source = "vehicle.name")
    @Mapping(target = "excursionId", source = "excursion.id")
    @Mapping(target = "excursionName", source = "excursion.name")
    ReservationDTO toDTO(Reservation reservation);
}
