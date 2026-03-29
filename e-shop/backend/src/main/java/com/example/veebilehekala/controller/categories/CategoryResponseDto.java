package com.example.veebilehekala.controller.categories;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CategoryResponseDto {
    @Schema(description = "Category ID", example = "1")
    private Integer id;
    @Schema(description = "Category name", example = "Books")
    private String name;
}
