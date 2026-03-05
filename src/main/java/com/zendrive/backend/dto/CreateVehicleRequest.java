package com.zendrive.backend.dto;

import com.zendrive.backend.enums.ComfortLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVehicleRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String brand;
    @NotBlank
    private String model;
    private String imageUrl;
    @Positive
    private int passengerCapacity;
    @Positive
    private int baggageCapacity;
    @NotNull
    private ComfortLevel comfortLevel;
    @NotNull @Positive
    private BigDecimal pricePerKm;
    private BigDecimal pricePerHour;
    private BigDecimal baseFare;
    private String licensePlate;
    private String description;
}
