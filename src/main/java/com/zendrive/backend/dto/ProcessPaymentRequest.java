package com.zendrive.backend.dto;

import com.zendrive.backend.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentRequest {
    @NotNull
    private Long reservationId;
    @NotNull
    private PaymentMethod paymentMethod;
    private String paymentToken;
}
