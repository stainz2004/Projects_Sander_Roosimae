package com.example.veebilehekala.controller.cartitem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartSummaryDto {
    @Schema(description = "Subtotal amount", example = "59.98")
    private float subtotal;
    @Schema(description = "Discount amount", example = "5.00")
    private float discount;
    @Schema(description = "Total amount", example = "54.98")
    private float total;
    @Schema(description = "Applied coupon code", example = "SUMMER21")
    private String couponCode;
}
