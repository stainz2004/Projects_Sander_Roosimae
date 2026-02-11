package com.example.veebilehekala.mapper;

import com.example.veebilehekala.controller.review.ReviewDto;
import com.example.veebilehekala.controller.review.ReviewResponseDto;
import com.example.veebilehekala.entity.Customer;
import com.example.veebilehekala.entity.Review;
import com.example.veebilehekala.entity.ShopItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {
    @Mapping(source = "shopItem.name", target = "shopItemName")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "shopItemId", source = "shopItem.id")
    ReviewResponseDto mapToDTO(Review review);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "customer", target = "customer")
    @Mapping(source = "shopItem", target = "shopItem")
    Review toEntity (ReviewDto dto, Customer customer, ShopItem shopItem);
}
