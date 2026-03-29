package com.example.veebilehekala.controller.review;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO used for creating a product review")
public class ReviewDto {

    @Schema(
            description = "Username of the customer writing the review",
            example = "john_doe"
    )
    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Schema(
            description = "ID of the shop item being reviewed",
            example = "42"
    )
    @NotNull(message = "Shop item id is required")
    private Integer shopItemId;

    @Schema(
            description = "Rating given to the product (1–5)",
            example = "5",
            minimum = "1",
            maximum = "5"
    )
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;

    @Schema(
            description = "Optional text comment for the review",
            example = "Great quality, fast delivery!"
    )
    private String comment;
}
