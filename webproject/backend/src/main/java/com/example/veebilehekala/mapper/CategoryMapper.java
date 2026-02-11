package com.example.veebilehekala.mapper;

import com.example.veebilehekala.controller.categories.CategoryResponseDto;
import com.example.veebilehekala.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    CategoryResponseDto mapToDTO(Category category);
}
