package com.example.veebilehekala.service;

import com.example.veebilehekala.controller.order.CreateOrderDto;
import com.example.veebilehekala.controller.order.OrderResponseDto;
import com.example.veebilehekala.entity.CartItem;
import com.example.veebilehekala.entity.Coupon;
import com.example.veebilehekala.entity.Customer;
import com.example.veebilehekala.entity.Order;
import com.example.veebilehekala.entity.OrderItem;
import com.example.veebilehekala.entity.ShopItem;
import com.example.veebilehekala.mapper.OrderMapper;
import com.example.veebilehekala.repository.CartItemRepository;
import com.example.veebilehekala.repository.CouponRepository;
import com.example.veebilehekala.repository.OrderRepository;
import com.example.veebilehekala.repository.SignInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository ordersRepository;
    private final SignInRepository signInRepository;
    private final CartItemRepository cartItemRepository;
    private final CouponRepository couponRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponseDto createOrder(String username, CreateOrderDto dto) {

        Customer customer = signInRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + username));

        Order order = new Order();
        order.setCustomer(customer);
        order.setCreatedAt(LocalDateTime.now());

        if (dto != null && dto.getCouponId() != null) {
            Coupon coupon = couponRepository.findById(dto.getCouponId())
                    .orElseThrow(() -> new IllegalArgumentException("Coupon not found: " + dto.getCouponId()));
            order.setCoupon(coupon);
        }

        float total = 0f;

        List<CartItem> cartItems = cartItemRepository.findByUserId(customer.getId());

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        for (CartItem cartItem : cartItems) {
            ShopItem shopItem = cartItem.getShopItem();
            OrderItem orderItem = orderMapper.toEntity(order, shopItem, cartItem);
            order.getItems().add(orderItem);

            total += shopItem.getPrice() * cartItem.getQuantity();
        }

        // apply coupon if present
        if (order.getCoupon() != null) {
            total = (total * (1 - order.getCoupon().getDiscount()));
        }

        order.setTotalPrice(total);

        Order saved = ordersRepository.save(order);
        cartItemRepository.deleteAll(cartItems);
        return orderMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Integer id) {
        Order order = ordersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        return orderMapper.toDto(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByCustomer(Authentication authentication) {
        Long id = getUserIdFromAuth(authentication);
        List<Order> orders = ordersRepository.findAllByCustomerId(id);
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    public Long getUserIdFromAuth(Authentication authentication) {
        String username = authentication.getName();
        return signInRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }
}
