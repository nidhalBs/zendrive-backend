package com.zendrive.backend.repository;

import com.zendrive.backend.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode(String code);
    Page<Coupon> findByActiveTrue(Pageable pageable);

    @Query("SELECT c FROM Coupon c WHERE c.code = :code AND c.active = true AND c.validFrom <= :now AND c.validUntil >= :now AND c.currentUsageCount < c.maxUsageCount")
    Optional<Coupon> findValidCoupon(@Param("code") String code, @Param("now") LocalDateTime now);
}
