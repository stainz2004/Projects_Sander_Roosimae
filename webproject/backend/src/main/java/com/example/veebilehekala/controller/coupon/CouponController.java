package com.example.veebilehekala.controller.coupon;

import com.example.veebilehekala.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
@Tag(name = "Coupons", description = "Coupons API")
public class CouponController {

    private final CouponService couponService;

    @PostMapping("admin/coupons")
    @Operation(summary = "Add coupon", description = "Add a new coupon")
    @ApiResponse(responseCode = "200", description = "Coupon added successfully")
    public ResponseEntity<CouponResponseDto> applyCoupon(@Valid @RequestBody CouponAddDto couponAddDto) {
        return ResponseEntity.ok(couponService.addCoupon(couponAddDto));
    }

    @GetMapping("public/coupons/{code}")
    @Operation(summary = "Get coupon by code", description = "Get coupon by code")
    @ApiResponse(responseCode = "200", description = "Found and return coupon")
    public ResponseEntity<CouponResponseDto> getCouponByCode(@PathVariable String code) {
        return ResponseEntity.ok(couponService.getCouponByCode(code));
    }
}
