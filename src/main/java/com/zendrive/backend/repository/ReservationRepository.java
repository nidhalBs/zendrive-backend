package com.zendrive.backend.repository;

import com.zendrive.backend.entity.Reservation;
import com.zendrive.backend.enums.ReservationStatus;
import com.zendrive.backend.enums.ReservationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByBookingReference(String bookingReference);
    Page<Reservation> findByClientId(Long clientId, Pageable pageable);
    Page<Reservation> findByStatus(ReservationStatus status, Pageable pageable);
    Page<Reservation> findByType(ReservationType type, Pageable pageable);
    Page<Reservation> findByTypeAndStatus(ReservationType type, ReservationStatus status, Pageable pageable);
    List<Reservation> findByPickupDateAndStatus(LocalDate pickupDate, ReservationStatus status);
    List<Reservation> findByVehicleIdAndPickupDate(Long vehicleId, LocalDate pickupDate);

    long countByStatus(ReservationStatus status);
    long countByType(ReservationType type);
    long countByTypeAndStatus(ReservationType type, ReservationStatus status);

    @Query("SELECT SUM(r.totalPrice) FROM Reservation r WHERE r.status = 'COMPLETED'")
    BigDecimal calculateTotalRevenue();

    @Query("SELECT SUM(r.totalPrice) FROM Reservation r WHERE r.status = 'COMPLETED' AND r.type = :type")
    BigDecimal calculateRevenueByType(@Param("type") ReservationType type);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.pickupDate BETWEEN :start AND :end")
    long countReservationsBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT SUM(r.totalPrice) FROM Reservation r WHERE r.status = 'COMPLETED' AND r.pickupDate BETWEEN :start AND :end")
    BigDecimal calculateRevenueBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
