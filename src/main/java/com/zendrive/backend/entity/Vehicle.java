package com.zendrive.backend.entity;

import com.zendrive.backend.enums.ComfortLevel;
import com.zendrive.backend.enums.VehicleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    private String imageUrl;

    @Column(nullable = false)
    private int passengerCapacity;

    @Column(nullable = false)
    private int baggageCapacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComfortLevel comfortLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerKm;

    @Column(precision = 10, scale = 2)
    private BigDecimal pricePerHour;

    @Column(precision = 10, scale = 2)
    private BigDecimal baseFare;

    private String licensePlate;

    private String description;

    private boolean active;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = VehicleStatus.AVAILABLE;
        }
        active = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
