package com.zendrive.backend.dto;

import com.zendrive.backend.enums.PaymentMethod;
import com.zendrive.backend.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Long id;
    private Long reservationId;
    private String bookingReference;
    private BigDecimal amount;
    private BigDecimal refundedAmount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private LocalDateTime refundedAt;
}
