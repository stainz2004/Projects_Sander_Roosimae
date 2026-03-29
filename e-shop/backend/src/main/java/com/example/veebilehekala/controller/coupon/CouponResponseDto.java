package com.example.veebilehekala.controller.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CouponResponseDto {
    @Schema(description = "Coupon code", example = "SUMMER2024")
    private String code;

    @Schema(description = "Discount percentage (0-1)", example = "0.15")
    private float discount;

    @Schema(description = "Success", example = "true")
    private boolean success;

}
