package com.example.veebilehekala.service;

import com.example.veebilehekala.controller.coupon.CouponAddDto;
import com.example.veebilehekala.controller.coupon.CouponResponseDto;
import com.example.veebilehekala.entity.Coupon;
import com.example.veebilehekala.exception.ApplicationException;
import com.example.veebilehekala.mapper.CouponMapper;
import com.example.veebilehekala.mapper.CouponMapperImpl;
import com.example.veebilehekala.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    @Spy
    private CouponMapper couponMapper = new CouponMapperImpl();

    private CouponResponseDto couponResponseDto;
    private String code;
    private Coupon coupon;
    private CouponAddDto couponAddDto;

    @BeforeEach
    void setUp() {
        couponResponseDto = new CouponResponseDto("123456",0.2f, true );
        code = "123456";
        coupon = Coupon.builder().id(1).code("123456").discount(0.2f).build();
        couponAddDto = CouponAddDto.builder().code("123456").discount(0.1f).build();
    }

    @Test
    void testGetCouponByCodeWorks() {
        given(couponRepository.findByCode(code))
                .willReturn(Optional.of(coupon));

        CouponResponseDto response = couponService.getCouponByCode(code);

        assertEquals(couponResponseDto, response);
    }

    @Test
    void testAddCouponExists() {
        given(couponRepository.findByCode(code)).willReturn(Optional.of(coupon));

        assertThrows(ApplicationException.class, () -> {
            couponService.addCoupon(couponAddDto);
        });
    }

    @Test
    void testAddCouponSuccessWorks() {
        given(couponRepository.findByCode(code)).willReturn(Optional.empty());
        given(couponRepository.save(any(Coupon.class))).willReturn(coupon);

        CouponResponseDto response = couponService.addCoupon(couponAddDto);

        assertEquals(couponResponseDto, response);
    }
}
