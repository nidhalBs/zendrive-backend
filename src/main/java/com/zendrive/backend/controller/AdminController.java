package com.zendrive.backend.controller;

import com.zendrive.backend.dto.*;
import com.zendrive.backend.entity.AuditLog;
import com.zendrive.backend.entity.User;
import com.zendrive.backend.enums.*;
import com.zendrive.backend.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final DashboardService dashboardService;
    private final ReservationService reservationService;
    private final VehicleService vehicleService;
    private final ExcursionService excursionService;
    private final UserService userService;
    private final ReviewService reviewService;
    private final PaymentService paymentService;
    private final CouponService couponService;
    private final NotificationService notificationService;
    private final RouteService routeService;
    private final AuditService auditService;

    // ========== DASHBOARD ==========
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardStatsDTO>> getDashboardStats() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getDashboardStats()));
    }

    // ========== RESERVATIONS ==========
    @GetMapping("/reservations")
    public ResponseEntity<ApiResponse<Page<ReservationDTO>>> getAllReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) ReservationType type) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ReservationDTO> reservations;
        if (status != null) {
            reservations = reservationService.getReservationsByStatus(status, pageRequest);
        } else if (type != null) {
            reservations = reservationService.getReservationsByType(type, pageRequest);
        } else {
            reservations = reservationService.getAllReservations(pageRequest);
        }
        return ResponseEntity.ok(ApiResponse.success(reservations));
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<ApiResponse<ReservationDTO>> getReservation(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(reservationService.getReservationById(id)));
    }

    @PutMapping("/reservations/{id}/status")
    public ResponseEntity<ApiResponse<ReservationDTO>> updateReservationStatus(
            @PathVariable Long id,
            @RequestParam ReservationStatus status,
            Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        auditService.log("UPDATE_STATUS", "Reservation", id, admin.getId(), admin.getEmail(),
                "Status changed to " + status);
        return ResponseEntity.ok(ApiResponse.success(reservationService.updateReservationStatus(id, status)));
    }

    @PutMapping("/reservations/{id}/notes")
    public ResponseEntity<ApiResponse<ReservationDTO>> addReservationNotes(
            @PathVariable Long id, @RequestParam String notes) {
        return ResponseEntity.ok(ApiResponse.success(reservationService.addAdminNotes(id, notes)));
    }

    // ========== VEHICLES ==========
    @GetMapping("/vehicles")
    public ResponseEntity<ApiResponse<Page<VehicleDTO>>> getAllVehicles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                vehicleService.getAllVehicles(PageRequest.of(page, size, Sort.by("createdAt").descending()))
        ));
    }

    @PostMapping("/vehicles")
    public ResponseEntity<ApiResponse<VehicleDTO>> createVehicle(
            @Valid @RequestBody CreateVehicleRequest request,
            Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        VehicleDTO vehicle = vehicleService.createVehicle(request);
        auditService.log("CREATE", "Vehicle", vehicle.getId(), admin.getId(), admin.getEmail(),
                "Vehicle created: " + request.getName());
        return ResponseEntity.ok(ApiResponse.success("Vehicle created", vehicle));
    }

    @PutMapping("/vehicles/{id}")
    public ResponseEntity<ApiResponse<VehicleDTO>> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody CreateVehicleRequest request,
            Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        auditService.log("UPDATE", "Vehicle", id, admin.getId(), admin.getEmail(),
                "Vehicle updated: " + request.getName());
        return ResponseEntity.ok(ApiResponse.success(vehicleService.updateVehicle(id, request)));
    }

    @PutMapping("/vehicles/{id}/status")
    public ResponseEntity<ApiResponse<VehicleDTO>> updateVehicleStatus(
            @PathVariable Long id, @RequestParam VehicleStatus status) {
        return ResponseEntity.ok(ApiResponse.success(vehicleService.updateVehicleStatus(id, status)));
    }

    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<ApiResponse<String>> deactivateVehicle(@PathVariable Long id) {
        vehicleService.deactivateVehicle(id);
        return ResponseEntity.ok(ApiResponse.success("Vehicle deactivated", null));
    }

    // ========== EXCURSIONS ==========
    @GetMapping("/excursions")
    public ResponseEntity<ApiResponse<Page<ExcursionDTO>>> getAllExcursions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                excursionService.getAllExcursions(PageRequest.of(page, size, Sort.by("createdAt").descending()))
        ));
    }

    @PostMapping("/excursions")
    public ResponseEntity<ApiResponse<ExcursionDTO>> createExcursion(
            @Valid @RequestBody CreateExcursionRequest request,
            Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        ExcursionDTO excursion = excursionService.createExcursion(request);
        auditService.log("CREATE", "Excursion", excursion.getId(), admin.getId(), admin.getEmail(),
                "Excursion created: " + request.getName());
        return ResponseEntity.ok(ApiResponse.success("Excursion created", excursion));
    }

    @PutMapping("/excursions/{id}")
    public ResponseEntity<ApiResponse<ExcursionDTO>> updateExcursion(
            @PathVariable Long id,
            @Valid @RequestBody CreateExcursionRequest request) {
        return ResponseEntity.ok(ApiResponse.success(excursionService.updateExcursion(id, request)));
    }

    @DeleteMapping("/excursions/{id}")
    public ResponseEntity<ApiResponse<String>> deactivateExcursion(@PathVariable Long id) {
        excursionService.deactivateExcursion(id);
        return ResponseEntity.ok(ApiResponse.success("Excursion deactivated", null));
    }

    // ========== USERS ==========
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) UserRole role) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        if (role != null) {
            return ResponseEntity.ok(ApiResponse.success(userService.getUsersByRole(role, pageRequest)));
        }
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers(pageRequest)));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(id)));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse<UserDTO>> updateUserRole(
            @PathVariable Long id, @RequestParam UserRole role,
            Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        auditService.log("UPDATE_ROLE", "User", id, admin.getId(), admin.getEmail(),
                "Role changed to " + role);
        return ResponseEntity.ok(ApiResponse.success(userService.updateUserRole(id, role)));
    }

    @PutMapping("/users/{id}/toggle-enabled")
    public ResponseEntity<ApiResponse<String>> toggleUserEnabled(@PathVariable Long id) {
        userService.toggleUserEnabled(id);
        return ResponseEntity.ok(ApiResponse.success("User status toggled", null));
    }

    // ========== REVIEWS ==========
    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<Page<ReviewDTO>>> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) ReviewStatus status) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        if (status != null) {
            if (status == ReviewStatus.PENDING) {
                return ResponseEntity.ok(ApiResponse.success(reviewService.getPendingReviews(pageRequest)));
            }
            return ResponseEntity.ok(ApiResponse.success(reviewService.getApprovedReviews(pageRequest)));
        }
        return ResponseEntity.ok(ApiResponse.success(reviewService.getAllReviews(pageRequest)));
    }

    @PutMapping("/reviews/{id}/moderate")
    public ResponseEntity<ApiResponse<ReviewDTO>> moderateReview(
            @PathVariable Long id,
            @RequestParam ReviewStatus status,
            @RequestParam(required = false) String adminResponse,
            Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        auditService.log("MODERATE", "Review", id, admin.getId(), admin.getEmail(),
                "Review " + status);
        return ResponseEntity.ok(ApiResponse.success(reviewService.moderateReview(id, status, adminResponse)));
    }

    // ========== PAYMENTS ==========
    @GetMapping("/payments")
    public ResponseEntity<ApiResponse<Page<PaymentDTO>>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) PaymentStatus status) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        if (status != null) {
            return ResponseEntity.ok(ApiResponse.success(paymentService.getPaymentsByStatus(status, pageRequest)));
        }
        return ResponseEntity.ok(ApiResponse.success(paymentService.getAllPayments(pageRequest)));
    }

    @PostMapping("/payments/{id}/refund")
    public ResponseEntity<ApiResponse<PaymentDTO>> refundPayment(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Payment refunded", paymentService.refundPayment(id)));
    }

    // ========== COUPONS ==========
    @GetMapping("/coupons")
    public ResponseEntity<ApiResponse<Page<CouponDTO>>> getAllCoupons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                couponService.getAllCoupons(PageRequest.of(page, size))
        ));
    }

    @PostMapping("/coupons")
    public ResponseEntity<ApiResponse<CouponDTO>> createCoupon(@Valid @RequestBody CouponDTO couponDTO) {
        return ResponseEntity.ok(ApiResponse.success("Coupon created", couponService.createCoupon(couponDTO)));
    }

    @PutMapping("/coupons/{id}")
    public ResponseEntity<ApiResponse<CouponDTO>> updateCoupon(
            @PathVariable Long id, @Valid @RequestBody CouponDTO couponDTO) {
        return ResponseEntity.ok(ApiResponse.success(couponService.updateCoupon(id, couponDTO)));
    }

    @DeleteMapping("/coupons/{id}")
    public ResponseEntity<ApiResponse<String>> deactivateCoupon(@PathVariable Long id) {
        couponService.deactivateCoupon(id);
        return ResponseEntity.ok(ApiResponse.success("Coupon deactivated", null));
    }

    // ========== ROUTES ==========
    @GetMapping("/routes")
    public ResponseEntity<ApiResponse<Page<RouteDTO>>> getAllRoutes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                routeService.getAllRoutes(PageRequest.of(page, size))
        ));
    }

    @PostMapping("/routes")
    public ResponseEntity<ApiResponse<RouteDTO>> createRoute(@Valid @RequestBody RouteDTO routeDTO) {
        return ResponseEntity.ok(ApiResponse.success("Route created", routeService.createRoute(routeDTO)));
    }

    @PutMapping("/routes/{id}")
    public ResponseEntity<ApiResponse<RouteDTO>> updateRoute(
            @PathVariable Long id, @Valid @RequestBody RouteDTO routeDTO) {
        return ResponseEntity.ok(ApiResponse.success(routeService.updateRoute(id, routeDTO)));
    }

    @DeleteMapping("/routes/{id}")
    public ResponseEntity<ApiResponse<String>> deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.ok(ApiResponse.success("Route deactivated", null));
    }

    // ========== NOTIFICATIONS ==========
    @GetMapping("/notifications")
    public ResponseEntity<ApiResponse<Page<NotificationDTO>>> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                notificationService.getAllNotifications(PageRequest.of(page, size, Sort.by("createdAt").descending()))
        ));
    }

    // ========== AUDIT LOGS ==========
    @GetMapping("/audit-logs")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String entityType) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        if (entityType != null) {
            return ResponseEntity.ok(ApiResponse.success(auditService.getAuditLogsByEntity(entityType, pageRequest)));
        }
        return ResponseEntity.ok(ApiResponse.success(auditService.getAuditLogs(pageRequest)));
    }
}
