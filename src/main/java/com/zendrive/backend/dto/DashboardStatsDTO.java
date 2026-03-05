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
public class DashboardStatsDTO {
    private long totalReservations;
    private long pendingReservations;
    private long confirmedReservations;
    private long completedReservations;
    private long cancelledReservations;
    private long totalTransfers;
    private long totalExcursions;
    private BigDecimal totalRevenue;
    private BigDecimal transferRevenue;
    private BigDecimal excursionRevenue;
    private long totalClients;
    private long activeClients;
    private long totalVehicles;
    private long availableVehicles;
    private long totalReviews;
    private long pendingReviews;
    private Double averageRating;
}
