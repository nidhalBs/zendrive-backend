package com.zendrive.backend.service;

import com.zendrive.backend.dto.PaymentDTO;
import com.zendrive.backend.dto.ProcessPaymentRequest;
import com.zendrive.backend.entity.Payment;
import com.zendrive.backend.entity.Reservation;
import com.zendrive.backend.enums.PaymentStatus;
import com.zendrive.backend.enums.ReservationStatus;
import com.zendrive.backend.mapper.PaymentMapper;
import com.zendrive.backend.repository.PaymentRepository;
import com.zendrive.backend.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    public PaymentDTO processPayment(ProcessPaymentRequest request) {
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (reservation.getPayment() != null && reservation.getPayment().getStatus() == PaymentStatus.COMPLETED) {
            throw new IllegalArgumentException("Reservation already paid");
        }

        Payment payment = Payment.builder()
                .reservation(reservation)
                .amount(reservation.getTotalPrice())
                .paymentMethod(request.getPaymentMethod())
                .status(PaymentStatus.COMPLETED)
                .transactionId(UUID.randomUUID().toString())
                .paidAt(LocalDateTime.now())
                .build();

        payment = paymentRepository.save(payment);

        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);

        return paymentMapper.toDTO(payment);
    }

    @Transactional
    public PaymentDTO refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new IllegalArgumentException("Can only refund completed payments");
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundedAmount(payment.getAmount());
        payment.setRefundedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        Reservation reservation = payment.getReservation();
        reservation.setStatus(ReservationStatus.REFUNDED);
        reservationRepository.save(reservation);

        return paymentMapper.toDTO(payment);
    }

    public PaymentDTO getPaymentByReservation(Long reservationId) {
        Payment payment = paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        return paymentMapper.toDTO(payment);
    }

    public Page<PaymentDTO> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable).map(paymentMapper::toDTO);
    }

    public Page<PaymentDTO> getPaymentsByStatus(PaymentStatus status, Pageable pageable) {
        return paymentRepository.findByStatus(status, pageable).map(paymentMapper::toDTO);
    }

    public BigDecimal getRevenueBetween(LocalDateTime start, LocalDateTime end) {
        BigDecimal revenue = paymentRepository.calculateRevenueBetween(start, end);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }
}
