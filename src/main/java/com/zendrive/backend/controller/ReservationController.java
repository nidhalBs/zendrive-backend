package com.zendrive.backend.controller;

import com.zendrive.backend.dto.ApiResponse;
import com.zendrive.backend.dto.CreateReservationRequest;
import com.zendrive.backend.dto.ReservationDTO;
import com.zendrive.backend.entity.User;
import com.zendrive.backend.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationDTO>> createReservation(
            @Valid @RequestBody CreateReservationRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ReservationDTO reservation = reservationService.createReservation(request, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Reservation created", reservation));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<ReservationDTO>>> getMyReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(
                reservationService.getReservationsByClient(user.getId(),
                        PageRequest.of(page, size, Sort.by("createdAt").descending()))
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservationDTO>> getReservation(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(reservationService.getReservationById(id)));
    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<ApiResponse<ReservationDTO>> getReservationByReference(@PathVariable String reference) {
        return ResponseEntity.ok(ApiResponse.success(reservationService.getReservationByReference(reference)));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<ReservationDTO>> cancelReservation(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(ApiResponse.success("Reservation cancelled",
                reservationService.cancelReservation(id, reason)));
    }
}
