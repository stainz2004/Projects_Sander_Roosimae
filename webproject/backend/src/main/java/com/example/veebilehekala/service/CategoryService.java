package com.example.veebilehekala.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.veebilehekala.controller.categories.CategoryDTO;
import com.example.veebilehekala.controller.categories.CategoryResponseDto;
import com.example.veebilehekala.entity.Category;
import com.example.veebilehekala.exception.ApplicationException;
import com.example.veebilehekala.mapper.CategoryMapper;
import com.example.veebilehekala.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryResponseDto(category.getId(),
                        category.getName()))
                .toList();
    }

    public CategoryResponseDto getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Category not found", 404));
        return categoryMapper.mapToDTO(category);
    }

    public CategoryResponseDto createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        Category savedCategory = categoryRepository.save(category);
        log.info("Category created: {}", savedCategory.getId());

        return new CategoryResponseDto(savedCategory.getId(),
                savedCategory.getName());

    }

}
