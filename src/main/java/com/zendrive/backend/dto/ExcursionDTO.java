package com.zendrive.backend.dto;

import com.zendrive.backend.enums.ExcursionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcursionDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private ExcursionType type;
    private BigDecimal basePrice;
    private BigDecimal estimatedDurationHours;
    private String zone;
    private boolean active;
    private int minPassengers;
    private int maxPassengers;
    private List<ExcursionStepDTO> steps;
}
