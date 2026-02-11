package com.example.veebilehekala.mapper;

import com.example.veebilehekala.controller.order.OrderResponseDto;
import com.example.veebilehekala.controller.orderitem.OrderItemResponseDto;
import com.example.veebilehekala.entity.CartItem;
import com.example.veebilehekala.entity.Order;
import com.example.veebilehekala.entity.OrderItem;
import com.example.veebilehekala.entity.ShopItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface OrderMapper {

    @Mapping(target = "customerName", source = "customer.username")
    @Mapping(target = "price", source = "totalPrice")
    @Mapping(target = "couponId", expression = "java(order.getCoupon() != null ? order.getCoupon().getId() : null)")
    @Mapping(target = "timestamp", source = "createdAt")
    OrderResponseDto toDto(Order order);

    @Mapping(target = "shopItemId", source = "shopItem.id")
    OrderItemResponseDto toDto(OrderItem item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", source = "order")
    @Mapping(target = "shopItem", source = "shopItem")
    @Mapping(target = "productName", source = "shopItem.name")
    @Mapping(target = "productCategory", source = "shopItem.category.name")
    @Mapping(target = "unitPrice", source = "shopItem.price")
    @Mapping(target = "quantity", source = "cartItem.quantity")
    OrderItem toEntity(Order order, ShopItem shopItem, CartItem cartItem);
}
