package com.example.veebilehekala.controller.shopitem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ShopItemResponseDto {
    @Schema(description = "Shop item id", example = "1")
    private Integer id;
    @Schema(description = "Shop item name", example = "Book")
    private String name;
    @Schema(description = "Shop item description", example = "This is a book")
    private String description;
    @Schema(description = "Shop item price", example = "10.00")
    private float price;
    @Schema(description = "Shop item category id", example = "1")
    private Integer categoryId;
    @Schema(description = "Shop item category name", example = "Books")
    private String categoryName;
    @Schema(description = "Shop item product url", example = "https://example.com/product")
    private String productUrl;
}
