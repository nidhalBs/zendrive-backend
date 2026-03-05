package com.zendrive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteDTO {
    private Long id;
    private String originName;
    private Double originLatitude;
    private Double originLongitude;
    private String destinationName;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private BigDecimal distanceKm;
    private BigDecimal estimatedDurationMinutes;
    private BigDecimal basePrice;
    private boolean popular;
    private boolean active;
    private String imageUrl;
}
