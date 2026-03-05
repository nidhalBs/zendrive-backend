package com.zendrive.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "excursion_steps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcursionStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "excursion_id", nullable = false)
    private Excursion excursion;

    @Column(nullable = false)
    private int stepOrder;

    @Column(nullable = false)
    private String locationName;

    private Double latitude;
    private Double longitude;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal durationMinutes;

    private String imageUrl;
}
