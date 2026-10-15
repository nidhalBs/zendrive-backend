package com.zendrive.backend.controller;

import com.zendrive.backend.dto.ApiResponse;
import com.zendrive.backend.dto.PaymentDTO;
import com.zendrive.backend.dto.ProcessPaymentRequest;
import com.zendrive.backend.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<ApiResponse<PaymentDTO>> processPayment(
            @Valid @RequestBody ProcessPaymentRequest request) {
        PaymentDTO payment = paymentService.processPayment(request);
        return ResponseEntity.ok(ApiResponse.success("Payment processed", payment));
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<ApiResponse<PaymentDTO>> getPaymentByReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.getPaymentByReservation(reservationId)));
    }
}
