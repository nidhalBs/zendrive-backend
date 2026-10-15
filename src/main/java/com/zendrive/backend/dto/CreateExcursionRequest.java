package com.zendrive.backend.dto;

import com.zendrive.backend.enums.ExcursionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateExcursionRequest {
    @NotBlank
    private String name;
    private String description;
    private String imageUrl;
    @NotNull
    private ExcursionType type;
    @NotNull
    private BigDecimal basePrice;
    private BigDecimal estimatedDurationHours;
    private String zone;
    private int minPassengers;
    private int maxPassengers;
    private List<ExcursionStepDTO> steps;
}
