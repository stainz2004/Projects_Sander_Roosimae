package com.example.veebilehekala.controller.order;

import com.example.veebilehekala.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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
@RequestMapping("/api/order")
@Tag(name = "Orders", description = "Order API")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create order", description = "Create a new order")
    @ApiResponse(responseCode = "201", description = "Order created")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto createOrder(@RequestBody(required = false) CreateOrderDto dto, Authentication authentication) {
        String username = authentication.getName();
        return orderService.createOrder(username, dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieve specific order details")
    @ApiResponse(responseCode = "200", description = "Order found")
    public OrderResponseDto getOrderById(@PathVariable Integer id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/me")
    @Operation(summary = "Purchase history", description = "Get all orders for a customer")
    @ApiResponse(responseCode = "200", description = "Orders returned")
    public List<OrderResponseDto> getOrdersByCustomer(Authentication authentication) {
        return orderService.getOrdersByCustomer(authentication);
    }
}
