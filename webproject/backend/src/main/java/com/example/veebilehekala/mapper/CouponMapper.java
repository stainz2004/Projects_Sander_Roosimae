package com.example.veebilehekala.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.veebilehekala.entity.Coupon;
import com.example.veebilehekala.controller.coupon.CouponAddDto;
import com.example.veebilehekala.controller.coupon.CouponResponseDto;

@Mapper(componentModel = "spring")
public interface CouponMapper {
    
    @Mapping(target = "success", constant = "true")
    CouponResponseDto toResponseDto(Coupon coupon);
    
    @Mapping(target = "id", ignore = true)
    Coupon toEntity(CouponAddDto dto);
}
