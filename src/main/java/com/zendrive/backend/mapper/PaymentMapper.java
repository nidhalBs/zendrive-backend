package com.zendrive.backend.mapper;

import com.zendrive.backend.dto.PaymentDTO;
import com.zendrive.backend.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "reservationId", source = "reservation.id")
    @Mapping(target = "bookingReference", source = "reservation.bookingReference")
    PaymentDTO toDTO(Payment payment);
}
