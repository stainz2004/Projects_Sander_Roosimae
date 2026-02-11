package com.example.veebilehekala.controller.categories;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDTO {
    @NotBlank(message = "Name is required")
    @Schema(description = "Category name", example = "Books")
    private String name;
}