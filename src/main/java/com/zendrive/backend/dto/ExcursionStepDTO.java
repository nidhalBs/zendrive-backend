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
public class ExcursionStepDTO {
    private Long id;
    private int stepOrder;
    private String locationName;
    private Double latitude;
    private Double longitude;
    private String description;
    private BigDecimal durationMinutes;
    private String imageUrl;
}
