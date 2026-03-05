package com.zendrive.backend.service;

import com.zendrive.backend.dto.DashboardStatsDTO;
import com.zendrive.backend.enums.ReservationStatus;
import com.zendrive.backend.enums.ReservationType;
import com.zendrive.backend.enums.VehicleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ReservationService reservationService;
    private final VehicleService vehicleService;
    private final UserService userService;
    private final ReviewService reviewService;

    public DashboardStatsDTO getDashboardStats() {
        return DashboardStatsDTO.builder()
                .totalReservations(
                        reservationService.countByStatus(ReservationStatus.PENDING) +
                        reservationService.countByStatus(ReservationStatus.CONFIRMED) +
                        reservationService.countByStatus(ReservationStatus.COMPLETED) +
                        reservationService.countByStatus(ReservationStatus.CANCELLED)
                )
                .pendingReservations(reservationService.countByStatus(ReservationStatus.PENDING))
                .confirmedReservations(reservationService.countByStatus(ReservationStatus.CONFIRMED))
                .completedReservations(reservationService.countByStatus(ReservationStatus.COMPLETED))
                .cancelledReservations(reservationService.countByStatus(ReservationStatus.CANCELLED))
                .totalTransfers(reservationService.countByType(ReservationType.TRANSFER))
                .totalExcursions(reservationService.countByType(ReservationType.EXCURSION))
                .totalRevenue(reservationService.getTotalRevenue())
                .transferRevenue(reservationService.getRevenueByType(ReservationType.TRANSFER))
                .excursionRevenue(reservationService.getRevenueByType(ReservationType.EXCURSION))
                .totalClients(userService.countClients())
                .activeClients(userService.countActiveUsers())
                .totalVehicles(vehicleService.countActive())
                .availableVehicles(vehicleService.countByStatus(VehicleStatus.AVAILABLE))
                .totalReviews(reviewService.countPending() + reviewService.countPending())
                .pendingReviews(reviewService.countPending())
                .averageRating(reviewService.getAverageRating())
                .build();
    }
}
