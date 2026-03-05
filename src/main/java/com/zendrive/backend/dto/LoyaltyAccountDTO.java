package com.zendrive.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoyaltyAccountDTO {
    private Long id;
    private Long userId;
    private int totalPoints;
    private int availablePoints;
    private List<LoyaltyTransactionDTO> recentTransactions;
}
