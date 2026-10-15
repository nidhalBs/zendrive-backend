package com.zendrive.backend.mapper;

import com.zendrive.backend.dto.ReviewDTO;
import com.zendrive.backend.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "clientName", expression = "java(review.getClient().getFirstName() + \" \" + review.getClient().getLastName())")
    @Mapping(target = "clientProfileImage", source = "client.profileImage")
    @Mapping(target = "reservationId", source = "reservation.id")
    @Mapping(target = "bookingReference", source = "reservation.bookingReference")
    ReviewDTO toDTO(Review review);
}
