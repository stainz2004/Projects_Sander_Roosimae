package com.example.veebilehekala.controller.review;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@Builder
public class ReviewResponseDto {
    @Schema(description = "Unique identifier of the review", example = "1")
    private Integer id;
    @Schema(description = "Name of the customer who wrote the review", example = "John Doe")
    private String customerName;
    @Schema(description = "Identifier of the shop item being reviewed", example = "101")
    private Integer shopItemId;
    @Schema(description = "Name of the shop item being reviewed", example = "Wireless Mouse")
    private String shopItemName;
    @Schema(description = "Rating given by the customer", example = "5")
    private Integer rating;
    @Schema(description = "Comment provided by the customer", example = "Great product, highly recommend!")
    private String comment;
    @Schema(description = "Timestamp when the review was created", example = "2024-06-15T14:30:00")
    private LocalDateTime createdAt;
}
