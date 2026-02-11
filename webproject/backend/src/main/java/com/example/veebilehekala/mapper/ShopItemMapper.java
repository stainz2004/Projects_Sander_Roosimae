package com.example.veebilehekala.mapper;

import com.example.veebilehekala.controller.shopitem.ShopItemDto;
import com.example.veebilehekala.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.example.veebilehekala.controller.shopitem.ShopItemResponseDto;
import com.example.veebilehekala.entity.ShopItem;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface ShopItemMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ShopItemResponseDto mapToDto(ShopItem shopItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "name", source = "dto.name")
    ShopItem toEntity(ShopItemDto dto, Category category);

    @Mapping(target = "id", source = "shopItem.id")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "productUrl", source = "shopItem.productUrl")
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "price", source = "dto.price")
    ShopItem updateEntity(ShopItemDto dto, Category category, ShopItem shopItem);
}
