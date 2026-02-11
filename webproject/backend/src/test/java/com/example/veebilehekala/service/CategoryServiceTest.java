package com.example.veebilehekala.service;

import com.example.veebilehekala.controller.categories.CategoryDTO;
import com.example.veebilehekala.controller.categories.CategoryResponseDto;
import com.example.veebilehekala.entity.Category;
import com.example.veebilehekala.mapper.CategoryMapper;
import com.example.veebilehekala.mapper.CategoryMapperImpl;
import com.example.veebilehekala.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Spy
    private CategoryMapper categoryMapper = new CategoryMapperImpl();

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryResponseDto categoryResponseDto;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1)
                .name("Test Category")
                .build();

        categoryResponseDto = CategoryResponseDto.builder()
                .id(1)
                .name("Test Category")
                .build();

        categoryDTO = CategoryDTO.builder()
                .name("Test Category")
                .build();
    }

    @Test
    void testGetAllCategoriesWorks() {
        //given
        given(categoryRepository.findAll())
                .willReturn(List.of(category));
        //when
        List<CategoryResponseDto> categoryResponseDtos = categoryService.getAllCategories();
        //then
        assertEquals(1, categoryResponseDtos.size());
        assertEquals(List.of(categoryResponseDto), categoryResponseDtos);
    }

    @Test
    void testGetCategoryByIdWorks() {
        given(categoryRepository.findById(1))
                .willReturn(Optional.of(category));
        CategoryResponseDto foundCategory = categoryService.getCategoryById(1);

        assertEquals(categoryResponseDto, foundCategory);

    }

    @Test
    void testCreateCategoryWorks() {
        given(categoryRepository.save(any(Category.class))).willReturn(category);

        CategoryResponseDto createdCategory = categoryService.createCategory(categoryDTO);

        assertEquals(categoryResponseDto, createdCategory);

    }
}
