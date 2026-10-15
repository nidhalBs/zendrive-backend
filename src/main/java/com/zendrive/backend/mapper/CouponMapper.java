package com.zendrive.backend.mapper;

import com.zendrive.backend.dto.CouponDTO;
import com.zendrive.backend.entity.Coupon;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CouponMapper {
    CouponDTO toDTO(Coupon coupon);
    Coupon toEntity(CouponDTO dto);
}
