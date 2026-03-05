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
public class PriceEstimateResponse {
    private BigDecimal estimatedDistance;
    private BigDecimal estimatedDuration;
    private BigDecimal basePrice;
    private BigDecimal discountAmount;
    private BigDecimal totalPrice;
    private String vehicleName;
    private String couponApplied;
}
