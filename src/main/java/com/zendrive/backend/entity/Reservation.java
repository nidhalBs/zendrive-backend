package com.zendrive.backend.entity;

import com.zendrive.backend.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String bookingReference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "excursion_id")
    private Excursion excursion;

    // Transfer fields
    private String pickupLocation;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private String dropoffLocation;
    private Double dropoffLatitude;
    private Double dropoffLongitude;

    @Enumerated(EnumType.STRING)
    private TransferType transferType;

    @Column(nullable = false)
    private LocalDate pickupDate;

    @Column(nullable = false)
    private LocalTime pickupTime;

    // Return trip (for round trip)
    private LocalDate returnDate;
    private LocalTime returnTime;

    private int passengerCount;

    // Pricing
    @Column(precision = 10, scale = 2)
    private BigDecimal estimatedDistance;

    @Column(precision = 10, scale = 2)
    private BigDecimal estimatedDuration;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount;

    // Flight tracking
    private String flightNumber;
    private String airlineName;

    // Client info for guest bookings
    private String guestEmail;
    private String guestPhone;
    private String guestName;

    private String specialRequests;
    private String adminNotes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime cancelledAt;
    private String cancellationReason;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Review review;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = ReservationStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
