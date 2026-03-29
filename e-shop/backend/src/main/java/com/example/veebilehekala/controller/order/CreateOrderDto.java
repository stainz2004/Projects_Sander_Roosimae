package com.example.veebilehekala.controller.order;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@Builder
public class CreateOrderDto {
    @Schema(description = "Optional coupon ID", example = "1")
    private Integer couponId;
}
