package com.zendrive.backend.controller;

import com.zendrive.backend.dto.ApiResponse;
import com.zendrive.backend.dto.CreateReviewRequest;
import com.zendrive.backend.dto.ReviewDTO;
import com.zendrive.backend.entity.User;
import com.zendrive.backend.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDTO>> createReview(
            @Valid @RequestBody CreateReviewRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ReviewDTO review = reviewService.createReview(request, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Review submitted for moderation", review));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<ReviewDTO>>> getMyReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(
                reviewService.getClientReviews(user.getId(),
                        PageRequest.of(page, size, Sort.by("createdAt").descending()))
        ));
    }
}
