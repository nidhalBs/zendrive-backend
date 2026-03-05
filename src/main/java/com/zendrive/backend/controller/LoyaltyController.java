package com.zendrive.backend.controller;

import com.zendrive.backend.dto.ApiResponse;
import com.zendrive.backend.dto.CouponDTO;
import com.zendrive.backend.dto.LoyaltyAccountDTO;
import com.zendrive.backend.dto.LoyaltyTransactionDTO;
import com.zendrive.backend.entity.User;
import com.zendrive.backend.service.CouponService;
import com.zendrive.backend.service.LoyaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loyalty")
@RequiredArgsConstructor
public class LoyaltyController {

    private final LoyaltyService loyaltyService;
    private final CouponService couponService;

    @GetMapping("/account")
    public ResponseEntity<ApiResponse<LoyaltyAccountDTO>> getMyLoyaltyAccount(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(loyaltyService.getAccountByUserId(user.getId())));
    }

    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<Page<LoyaltyTransactionDTO>>> getMyTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(ApiResponse.success(
                loyaltyService.getTransactionHistory(user.getId(),
                        PageRequest.of(page, size, Sort.by("createdAt").descending()))
        ));
    }

    @GetMapping("/coupons/validate/{code}")
    public ResponseEntity<ApiResponse<CouponDTO>> validateCoupon(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.success(couponService.validateCoupon(code)));
    }
}
