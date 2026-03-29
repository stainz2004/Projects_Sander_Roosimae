package com.example.veebilehekala.service;

import com.example.veebilehekala.mapper.CouponMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.veebilehekala.repository.CouponRepository;
import com.example.veebilehekala.entity.Coupon;
import com.example.veebilehekala.controller.coupon.CouponResponseDto;
import com.example.veebilehekala.exception.ApplicationException;
import com.example.veebilehekala.controller.coupon.CouponAddDto;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;


    public CouponResponseDto getCouponByCode(String code) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new ApplicationException("Coupon not found", 404));
        return couponMapper.toResponseDto(coupon);
    }

    public CouponResponseDto addCoupon(CouponAddDto couponAddDto) {
        Optional<Coupon> coupon = couponRepository.findByCode(couponAddDto.getCode());
        if (coupon.isPresent()) {
            throw new ApplicationException("Coupon already exists", 409);
        }
        Coupon newCoupon = couponMapper.toEntity(couponAddDto);
        Coupon saved = couponRepository.save(newCoupon);
        log.info("Coupon added: {}", saved.getCode());
        return couponMapper.toResponseDto(saved);
    }
}
