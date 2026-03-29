package com.example.veebilehekala.controller.categories;


import com.example.veebilehekala.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
@Tag(name = "Categories", description = "Categories API")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("public/categories")
    @Operation(summary = "Get all categories", description = "Get all categories")
    @ApiResponse(responseCode = "200", description = "Found and return categories")
    public List<CategoryResponseDto> getCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("public/categories/{id}")
    @Operation(summary = "Get category by id", description = "Get category by id")
    @ApiResponse(responseCode = "200", description = "Found and return category")
    public CategoryResponseDto getCategoryById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping("admin/categories")
    @Operation(summary = "Create category", description = "Create new category")
    @ApiResponse(responseCode = "201", description = "Category created")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        return categoryService.createCategory(categoryDTO);
    }
}
