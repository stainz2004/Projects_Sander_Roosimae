package com.example.veebilehekala.controller.shopitem;

import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
public class ShopItemDto {
    @Schema(description = "Shop item id", example = "1")
    private Integer id;
    @Schema(description = "Shop item name", example = "Book")
    @NotBlank(message = "Name is required")
    private String name;
    @Schema(description = "Shop item description", example = "This is a book")
    @NotBlank(message = "Description is required")
    private String description;
    @Schema(description = "Shop item price", example = "10.00")
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than 0")
    private float price;
    @Schema(description = "Shop item category id", example = "1")
    @NotNull(message = "Category is required")
    private Integer categoryId;
    @Schema(description = "Shop item product url", example = "https://example.com/product")
    private String productUrl;
}
