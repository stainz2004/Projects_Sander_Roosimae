package com.example.veebilehekala.controller.cartitem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponseDto {
    @Schema(description = "Cart item id", example = "1")
    private Integer id;
    @Schema(description = "User id", example = "1")
    private Long userId;
    @Schema(description = "Shop item id", example = "1")
    private Integer shopItemId;
    @Schema(description = "Product name", example = "Sample Product")
    private String name;
    @Schema(description = "Product description", example = "This is a sample product.")
    private String description;
    @Schema(description = "Product price", example = "19.99")
    private Float price;
    @Schema(description = "Quantity", example = "2")
    private Integer quantity;
    @Schema(description = "Total price", example = "39.98")
    private Float totalPrice;
    @Schema(description = "Product image URL", example = "http://example.com/image.jpg")
    private String productUrl;
}
