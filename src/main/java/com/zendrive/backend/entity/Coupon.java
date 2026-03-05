package com.zendrive.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(precision = 10, scale = 2)
    private BigDecimal maxDiscountAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal minOrderAmount;

    @Column(nullable = false)
    private int maxUsageCount;

    @Column(nullable = false)
    private int currentUsageCount;

    private boolean singleUsePerClient;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private LocalDateTime validFrom;

    @Column(nullable = false)
    private LocalDateTime validUntil;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        currentUsageCount = 0;
    }
}
