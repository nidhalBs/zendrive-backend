package com.zendrive.backend.repository;

import com.zendrive.backend.entity.Review;
import com.zendrive.backend.enums.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByStatus(ReviewStatus status, Pageable pageable);
    Page<Review> findByClientId(Long clientId, Pageable pageable);
    Optional<Review> findByReservationId(Long reservationId);
    boolean existsByClientIdAndReservationId(Long clientId, Long reservationId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.status = 'APPROVED'")
    Double calculateAverageRating();

    long countByStatus(ReviewStatus status);
}
