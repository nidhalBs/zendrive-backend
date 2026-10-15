package com.zendrive.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "routes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originName;

    private Double originLatitude;
    private Double originLongitude;

    @Column(nullable = false)
    private String destinationName;

    private Double destinationLatitude;
    private Double destinationLongitude;

    @Column(precision = 10, scale = 2)
    private BigDecimal distanceKm;

    @Column(precision = 10, scale = 2)
    private BigDecimal estimatedDurationMinutes;

    @Column(precision = 10, scale = 2)
    private BigDecimal basePrice;

    private boolean popular;
    private boolean active;

    private String imageUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        active = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
