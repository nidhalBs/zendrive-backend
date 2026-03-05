package com.zendrive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponDTO {
    private Long id;
    private String code;
    private String description;
    private BigDecimal discountPercentage;
    private BigDecimal maxDiscountAmount;
    private BigDecimal minOrderAmount;
    private int maxUsageCount;
    private int currentUsageCount;
    private boolean singleUsePerClient;
    private boolean active;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
}
