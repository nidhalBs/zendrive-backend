package com.zendrive.backend.dto;

import com.zendrive.backend.enums.TransferType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceEstimateRequest {
    private Double pickupLatitude;
    private Double pickupLongitude;
    private Double dropoffLatitude;
    private Double dropoffLongitude;
    @NotNull
    private Long vehicleId;
    @Positive
    private int passengerCount;
    private TransferType transferType;
    private String couponCode;
}
