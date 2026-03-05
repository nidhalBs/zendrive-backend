package com.zendrive.backend.entity;

import com.zendrive.backend.enums.ExcursionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "excursions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Excursion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExcursionType type;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal estimatedDurationHours;

    private String zone;

    private boolean active;

    private int minPassengers;
    private int maxPassengers;

    @OneToMany(mappedBy = "excursion", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("stepOrder ASC")
    private List<ExcursionStep> steps = new ArrayList<>();

    @OneToMany(mappedBy = "excursion", fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

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
