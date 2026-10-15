package com.zendrive.backend.service;

import com.zendrive.backend.dto.CreateReviewRequest;
import com.zendrive.backend.dto.ReviewDTO;
import com.zendrive.backend.entity.Reservation;
import com.zendrive.backend.entity.Review;
import com.zendrive.backend.entity.User;
import com.zendrive.backend.enums.ReservationStatus;
import com.zendrive.backend.enums.ReviewStatus;
import com.zendrive.backend.mapper.ReviewMapper;
import com.zendrive.backend.repository.ReservationRepository;
import com.zendrive.backend.repository.ReviewRepository;
import com.zendrive.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewDTO createReview(CreateReviewRequest request, Long clientId) {
        if (reviewRepository.existsByClientIdAndReservationId(clientId, request.getReservationId())) {
            throw new IllegalArgumentException("You have already reviewed this reservation");
        }

        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.COMPLETED) {
            throw new IllegalArgumentException("Can only review completed reservations");
        }

        Review review = Review.builder()
                .client(client)
                .reservation(reservation)
                .rating(request.getRating())
                .comment(request.getComment())
                .status(ReviewStatus.PENDING)
                .build();

        return reviewMapper.toDTO(reviewRepository.save(review));
    }

    public Page<ReviewDTO> getApprovedReviews(Pageable pageable) {
        return reviewRepository.findByStatus(ReviewStatus.APPROVED, pageable).map(reviewMapper::toDTO);
    }

    public Page<ReviewDTO> getPendingReviews(Pageable pageable) {
        return reviewRepository.findByStatus(ReviewStatus.PENDING, pageable).map(reviewMapper::toDTO);
    }

    public Page<ReviewDTO> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable).map(reviewMapper::toDTO);
    }

    public Page<ReviewDTO> getClientReviews(Long clientId, Pageable pageable) {
        return reviewRepository.findByClientId(clientId, pageable).map(reviewMapper::toDTO);
    }

    @Transactional
    public ReviewDTO moderateReview(Long id, ReviewStatus status, String adminResponse) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        review.setStatus(status);
        review.setAdminResponse(adminResponse);
        review.setModeratedAt(LocalDateTime.now());
        return reviewMapper.toDTO(reviewRepository.save(review));
    }

    public Double getAverageRating() {
        return reviewRepository.calculateAverageRating();
    }

    public long countPending() {
        return reviewRepository.countByStatus(ReviewStatus.PENDING);
    }
}
