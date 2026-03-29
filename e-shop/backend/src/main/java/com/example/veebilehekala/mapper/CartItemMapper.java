package com.example.veebilehekala.mapper;

import com.example.veebilehekala.controller.cartitem.AddToCartDto;
import com.example.veebilehekala.controller.cartitem.CartItemResponseDto;
import com.example.veebilehekala.entity.CartItem;
import com.example.veebilehekala.entity.ShopItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "shopItemId", source = "shopItem.id")
    @Mapping(target = "name", source = "shopItem.name")
    @Mapping(target = "description", source = "shopItem.description")
    @Mapping(target = "price", source = "shopItem.price")
    @Mapping(target = "productUrl", source = "shopItem.productUrl")
    @Mapping(target = "totalPrice", expression = "java(item.getQuantity() * item.getShopItem().getPrice())")
    CartItemResponseDto toResponseDto(CartItem item);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "dto.quantity", target = "quantity")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "shopItem", target = "shopItem")
    CartItem toEntity(AddToCartDto dto, Long userId, ShopItem shopItem);
}
