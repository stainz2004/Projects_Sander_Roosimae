package com.example.veebilehekala.controller.coupon;

import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
public class CouponAddDto {
    @NotBlank(message = "Code is required")
    @Schema(description = "Coupon code", example = "123456")
    private String code;
    @NotNull(message = "Discount is required")
    @Min(value = 0, message = "Discount must be greater than 0")
    @Max(value = 1, message = "Discount must be less than 1")
    private float discount;
}
