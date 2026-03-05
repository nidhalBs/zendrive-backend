package com.zendrive.backend.dto;

import com.zendrive.backend.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDTO {
    private Long id;
    private String bookingReference;
    private ReservationType type;
    private ReservationStatus status;
    private Long clientId;
    private String clientName;
    private String clientEmail;
    private Long vehicleId;
    private String vehicleName;
    private Long excursionId;
    private String excursionName;
    private String pickupLocation;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private String dropoffLocation;
    private Double dropoffLatitude;
    private Double dropoffLongitude;
    private TransferType transferType;
    private LocalDate pickupDate;
    private LocalTime pickupTime;
    private LocalDate returnDate;
    private LocalTime returnTime;
    private int passengerCount;
    private BigDecimal estimatedDistance;
    private BigDecimal estimatedDuration;
    private BigDecimal totalPrice;
    private BigDecimal discountAmount;
    private String flightNumber;
    private String airlineName;
    private String guestEmail;
    private String guestPhone;
    private String guestName;
    private String specialRequests;
    private String adminNotes;
    private LocalDateTime createdAt;
    private LocalDateTime cancelledAt;
    private String cancellationReason;
}
