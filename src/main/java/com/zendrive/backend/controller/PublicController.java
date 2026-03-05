package com.zendrive.backend.controller;

import com.zendrive.backend.dto.*;
import com.zendrive.backend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

    private final VehicleService vehicleService;
    private final ExcursionService excursionService;
    private final ReviewService reviewService;
    private final RouteService routeService;

    @GetMapping("/vehicles")
    public ResponseEntity<ApiResponse<List<VehicleDTO>>> getVehicles() {
        return ResponseEntity.ok(ApiResponse.success(vehicleService.getAllActiveVehicles()));
    }

    @GetMapping("/vehicles/available")
    public ResponseEntity<ApiResponse<List<VehicleDTO>>> getAvailableVehicles(
            @RequestParam(defaultValue = "1") int passengers) {
        return ResponseEntity.ok(ApiResponse.success(vehicleService.getAvailableVehicles(passengers)));
    }

    @GetMapping("/excursions")
    public ResponseEntity<ApiResponse<List<ExcursionDTO>>> getExcursions() {
        return ResponseEntity.ok(ApiResponse.success(excursionService.getActiveExcursions()));
    }

    @GetMapping("/excursions/{id}")
    public ResponseEntity<ApiResponse<ExcursionDTO>> getExcursion(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(excursionService.getExcursionById(id)));
    }

    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<Page<ReviewDTO>>> getReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                reviewService.getApprovedReviews(PageRequest.of(page, size, Sort.by("createdAt").descending()))
        ));
    }

    @GetMapping("/routes")
    public ResponseEntity<ApiResponse<List<RouteDTO>>> getRoutes() {
        return ResponseEntity.ok(ApiResponse.success(routeService.getActiveRoutes()));
    }

    @GetMapping("/routes/popular")
    public ResponseEntity<ApiResponse<List<RouteDTO>>> getPopularRoutes() {
        return ResponseEntity.ok(ApiResponse.success(routeService.getPopularRoutes()));
    }
}
