package com.example.veebilehekala.controller.order;

import com.example.veebilehekala.controller.orderitem.OrderItemResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    @Schema(description = "Order ID", example = "1")
    private Integer id;
    @Schema(description = "Customer name", example = "John Doe")
    private String customerName;
    @Schema(description = "Total price of the order", example = "99.99")
    private float price;
    @Schema(description = "Applied coupon ID, if any", example = "1")
    private Integer couponId;
    @Schema(description = "Timestamp of the order", example = "2024-06-01T12:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "List of items in the order")
    private List<OrderItemResponseDto> items;
}
