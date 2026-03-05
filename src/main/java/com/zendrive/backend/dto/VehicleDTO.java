package com.zendrive.backend.dto;

import com.zendrive.backend.enums.ComfortLevel;
import com.zendrive.backend.enums.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDTO {
    private Long id;
    private String name;
    private String brand;
    private String model;
    private String imageUrl;
    private int passengerCapacity;
    private int baggageCapacity;
    private ComfortLevel comfortLevel;
    private VehicleStatus status;
    private BigDecimal pricePerKm;
    private BigDecimal pricePerHour;
    private BigDecimal baseFare;
    private String licensePlate;
    private String description;
    private boolean active;
}
