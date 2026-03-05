package com.zendrive.backend.service;

import com.zendrive.backend.dto.CreateReservationRequest;
import com.zendrive.backend.dto.ReservationDTO;
import com.zendrive.backend.entity.*;
import com.zendrive.backend.enums.ReservationStatus;
import com.zendrive.backend.enums.ReservationType;
import com.zendrive.backend.mapper.ReservationMapper;
import com.zendrive.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final ExcursionRepository excursionRepository;
    private final CouponRepository couponRepository;
    private final ReservationMapper reservationMapper;

    @Transactional
    public ReservationDTO createReservation(CreateReservationRequest request, Long clientId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        if (request.getPassengerCount() > vehicle.getPassengerCapacity()) {
            throw new IllegalArgumentException("Vehicle capacity exceeded");
        }

        BigDecimal totalPrice = calculatePrice(request, vehicle);

        // Apply coupon if provided
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (request.getCouponCode() != null && !request.getCouponCode().isEmpty()) {
            Coupon coupon = couponRepository.findValidCoupon(request.getCouponCode(), java.time.LocalDateTime.now())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid or expired coupon"));
            discountAmount = totalPrice.multiply(coupon.getDiscountPercentage())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            if (coupon.getMaxDiscountAmount() != null && discountAmount.compareTo(coupon.getMaxDiscountAmount()) > 0) {
                discountAmount = coupon.getMaxDiscountAmount();
            }
            coupon.setCurrentUsageCount(coupon.getCurrentUsageCount() + 1);
        }

        totalPrice = totalPrice.subtract(discountAmount);

        Excursion excursion = null;
        if (request.getExcursionId() != null) {
            excursion = excursionRepository.findById(request.getExcursionId()).orElse(null);
        }

        Reservation reservation = Reservation.builder()
                .bookingReference(generateBookingReference())
                .type(request.getType())
                .status(ReservationStatus.PENDING)
                .client(client)
                .vehicle(vehicle)
                .excursion(excursion)
                .pickupLocation(request.getPickupLocation())
                .pickupLatitude(request.getPickupLatitude())
                .pickupLongitude(request.getPickupLongitude())
                .dropoffLocation(request.getDropoffLocation())
                .dropoffLatitude(request.getDropoffLatitude())
                .dropoffLongitude(request.getDropoffLongitude())
                .transferType(request.getTransferType())
                .pickupDate(request.getPickupDate())
                .pickupTime(request.getPickupTime())
                .returnDate(request.getReturnDate())
                .returnTime(request.getReturnTime())
                .passengerCount(request.getPassengerCount())
                .totalPrice(totalPrice)
                .discountAmount(discountAmount)
                .flightNumber(request.getFlightNumber())
                .airlineName(request.getAirlineName())
                .guestEmail(request.getGuestEmail())
                .guestPhone(request.getGuestPhone())
                .guestName(request.getGuestName())
                .specialRequests(request.getSpecialRequests())
                .build();

        // Calculate estimated distance & duration
        if (request.getPickupLatitude() != null && request.getDropoffLatitude() != null) {
            BigDecimal distance = calculateDistance(
                    request.getPickupLatitude(), request.getPickupLongitude(),
                    request.getDropoffLatitude(), request.getDropoffLongitude()
            );
            reservation.setEstimatedDistance(distance);
            reservation.setEstimatedDuration(distance.multiply(BigDecimal.valueOf(1.2)));
        }

        return reservationMapper.toDTO(reservationRepository.save(reservation));
    }

    public ReservationDTO getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        return reservationMapper.toDTO(reservation);
    }

    public ReservationDTO getReservationByReference(String reference) {
        Reservation reservation = reservationRepository.findByBookingReference(reference)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        return reservationMapper.toDTO(reservation);
    }

    public Page<ReservationDTO> getReservationsByClient(Long clientId, Pageable pageable) {
        return reservationRepository.findByClientId(clientId, pageable).map(reservationMapper::toDTO);
    }

    public Page<ReservationDTO> getAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable).map(reservationMapper::toDTO);
    }

    public Page<ReservationDTO> getReservationsByStatus(ReservationStatus status, Pageable pageable) {
        return reservationRepository.findByStatus(status, pageable).map(reservationMapper::toDTO);
    }

    public Page<ReservationDTO> getReservationsByType(ReservationType type, Pageable pageable) {
        return reservationRepository.findByType(type, pageable).map(reservationMapper::toDTO);
    }

    @Transactional
    public ReservationDTO updateReservationStatus(Long id, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        reservation.setStatus(status);
        if (status == ReservationStatus.CANCELLED) {
            reservation.setCancelledAt(java.time.LocalDateTime.now());
        }
        return reservationMapper.toDTO(reservationRepository.save(reservation));
    }

    @Transactional
    public ReservationDTO cancelReservation(Long id, String reason) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledAt(java.time.LocalDateTime.now());
        reservation.setCancellationReason(reason);
        return reservationMapper.toDTO(reservationRepository.save(reservation));
    }

    @Transactional
    public ReservationDTO addAdminNotes(Long id, String notes) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        reservation.setAdminNotes(notes);
        return reservationMapper.toDTO(reservationRepository.save(reservation));
    }

    public BigDecimal getTotalRevenue() {
        BigDecimal revenue = reservationRepository.calculateTotalRevenue();
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    public BigDecimal getRevenueByType(ReservationType type) {
        BigDecimal revenue = reservationRepository.calculateRevenueByType(type);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    public BigDecimal getRevenueBetween(LocalDate start, LocalDate end) {
        BigDecimal revenue = reservationRepository.calculateRevenueBetween(start, end);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    public long countByStatus(ReservationStatus status) {
        return reservationRepository.countByStatus(status);
    }

    public long countByType(ReservationType type) {
        return reservationRepository.countByType(type);
    }

    private BigDecimal calculatePrice(CreateReservationRequest request, Vehicle vehicle) {
        BigDecimal baseFare = vehicle.getBaseFare() != null ? vehicle.getBaseFare() : BigDecimal.ZERO;

        if (request.getPickupLatitude() != null && request.getDropoffLatitude() != null) {
            BigDecimal distance = calculateDistance(
                    request.getPickupLatitude(), request.getPickupLongitude(),
                    request.getDropoffLatitude(), request.getDropoffLongitude()
            );
            BigDecimal distancePrice = distance.multiply(vehicle.getPricePerKm());
            return baseFare.add(distancePrice).setScale(2, RoundingMode.HALF_UP);
        }

        return baseFare.add(vehicle.getPricePerKm().multiply(BigDecimal.valueOf(50)))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return BigDecimal.valueOf(earthRadius * c).setScale(2, RoundingMode.HALF_UP);
    }

    private String generateBookingReference() {
        return "ZD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
