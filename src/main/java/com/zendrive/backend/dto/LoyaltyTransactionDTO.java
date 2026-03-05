package com.zendrive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoyaltyTransactionDTO {
    private Long id;
    private int points;
    private String description;
    private String referenceType;
    private Long referenceId;
    private LocalDateTime createdAt;
}
