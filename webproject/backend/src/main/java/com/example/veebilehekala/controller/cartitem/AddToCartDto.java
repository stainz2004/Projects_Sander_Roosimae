package com.example.veebilehekala.controller.cartitem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddToCartDto {
    @Schema(description = "Shop item id", example = "1")
    private Integer shopItemId;
    @Schema(description = "Quantity", example = "1")
    private Integer quantity;
}
