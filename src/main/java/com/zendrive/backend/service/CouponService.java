package com.zendrive.backend.service;

import com.zendrive.backend.dto.CouponDTO;
import com.zendrive.backend.entity.Coupon;
import com.zendrive.backend.mapper.CouponMapper;
import com.zendrive.backend.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    public Page<CouponDTO> getAllCoupons(Pageable pageable) {
        return couponRepository.findAll(pageable).map(couponMapper::toDTO);
    }

    public Page<CouponDTO> getActiveCoupons(Pageable pageable) {
        return couponRepository.findByActiveTrue(pageable).map(couponMapper::toDTO);
    }

    public CouponDTO validateCoupon(String code) {
        Coupon coupon = couponRepository.findValidCoupon(code, LocalDateTime.now())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired coupon"));
        return couponMapper.toDTO(coupon);
    }

    @Transactional
    public CouponDTO createCoupon(CouponDTO couponDTO) {
        if (couponRepository.findByCode(couponDTO.getCode()).isPresent()) {
            throw new IllegalArgumentException("Coupon code already exists");
        }
        Coupon coupon = couponMapper.toEntity(couponDTO);
        coupon.setActive(true);
        return couponMapper.toDTO(couponRepository.save(coupon));
    }

    @Transactional
    public CouponDTO updateCoupon(Long id, CouponDTO couponDTO) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));
        coupon.setDescription(couponDTO.getDescription());
        coupon.setDiscountPercentage(couponDTO.getDiscountPercentage());
        coupon.setMaxDiscountAmount(couponDTO.getMaxDiscountAmount());
        coupon.setMinOrderAmount(couponDTO.getMinOrderAmount());
        coupon.setMaxUsageCount(couponDTO.getMaxUsageCount());
        coupon.setSingleUsePerClient(couponDTO.isSingleUsePerClient());
        coupon.setActive(couponDTO.isActive());
        coupon.setValidFrom(couponDTO.getValidFrom());
        coupon.setValidUntil(couponDTO.getValidUntil());
        return couponMapper.toDTO(couponRepository.save(coupon));
    }

    @Transactional
    public void deactivateCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));
        coupon.setActive(false);
        couponRepository.save(coupon);
    }
}
