package com.zendrive.backend.dto;

import com.zendrive.backend.enums.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private Long id;
    private Long clientId;
    private String clientName;
    private String clientProfileImage;
    private Long reservationId;
    private String bookingReference;
    private int rating;
    private String comment;
    private ReviewStatus status;
    private String adminResponse;
    private LocalDateTime createdAt;
    private LocalDateTime moderatedAt;
}
