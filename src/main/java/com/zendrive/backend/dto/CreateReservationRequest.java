package com.zendrive.backend.dto;

import com.zendrive.backend.enums.ReservationType;
import com.zendrive.backend.enums.TransferType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationRequest {
    @NotNull
    private ReservationType type;
    @NotBlank
    private String pickupLocation;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private String dropoffLocation;
    private Double dropoffLatitude;
    private Double dropoffLongitude;
    private TransferType transferType;
    @NotNull
    private LocalDate pickupDate;
    @NotNull
    private LocalTime pickupTime;
    private LocalDate returnDate;
    private LocalTime returnTime;
    @Positive
    private int passengerCount;
    @NotNull
    private Long vehicleId;
    private Long excursionId;
    private String flightNumber;
    private String airlineName;
    private String guestEmail;
    private String guestPhone;
    private String guestName;
    private String specialRequests;
    private String couponCode;
}
