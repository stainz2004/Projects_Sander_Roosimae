package com.example.veebilehekala.controller.orderitem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class OrderItemResponseDto {
    @Schema(description = "Order item ID", example = "1")
    private Integer id;
    @Schema(description = "Shop item ID", example = "3")
    private Integer shopItemId;
    @Schema(description = "Product name", example = "Wireless Mouse")
    private String productName;
    @Schema(description = "Product category", example = "Electronics")
    private String productCategory;
    @Schema(description = "Unit price of the item", example = "29.99")
    private double unitPrice;
    @Schema(description = "Quantity of the item", example = "2")
    private int quantity;
}
