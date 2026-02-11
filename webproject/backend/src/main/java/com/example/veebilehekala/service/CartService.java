package com.example.veebilehekala.service;

import com.example.veebilehekala.controller.cartitem.AddToCartDto;
import com.example.veebilehekala.controller.cartitem.CartItemResponseDto;
import com.example.veebilehekala.controller.cartitem.CartSummaryDto;
import com.example.veebilehekala.entity.CartItem;
import com.example.veebilehekala.entity.Coupon;
import com.example.veebilehekala.entity.ShopItem;
import com.example.veebilehekala.mapper.CartItemMapper;
import com.example.veebilehekala.repository.CartItemRepository;
import com.example.veebilehekala.repository.CouponRepository;
import com.example.veebilehekala.repository.ShopItemRepository;
import com.example.veebilehekala.repository.SignInRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private static final String NOTFOUNDMESSAGE = "Cart item not found";

    private final CartItemRepository cartItemRepository;
    private final ShopItemRepository shopItemRepository;
    private final SignInRepository signInRepository;
    private final CartItemMapper cartItemMapper;
    private final CouponRepository couponRepository;

    @Transactional
    public CartItemResponseDto addToCart(Long userId,AddToCartDto dto) {
        ShopItem shopItem = shopItemRepository.findById(dto.getShopItemId())
                .orElseThrow(() -> new IllegalArgumentException(NOTFOUNDMESSAGE));

        Optional<CartItem> existingItem = cartItemRepository
                .findByUserIdAndShopItemId(userId, dto.getShopItemId());

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + dto.getQuantity());
            CartItem updated = cartItemRepository.save(cartItem);
            return cartItemMapper.toResponseDto(updated);
        }
        CartItem cartItem = cartItemMapper.toEntity(dto, userId, shopItem);
        CartItem saved = cartItemRepository.save(cartItem);
        log.info("Cart item saved: {}", saved.getId());
        return cartItemMapper.toResponseDto(saved);
    }

    public List<CartItemResponseDto> getCartByUserId(Long userId) {
        return cartItemRepository.findByUserId(userId)
                .stream()
                .map(cartItemMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public void deleteCartItem(Long userId ,Integer cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException(NOTFOUNDMESSAGE));

        if (!item.getUserId().equals(userId)) {
            throw new IllegalArgumentException("This item does not belong to you");
        }
        log.info("Cart item deleted: {}", item.getId());
        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
        log.info("Cart cleared for user: {}", userId);
    }

    public Long getUserIdFromAuth(Authentication authentication) {
        String username = authentication.getName();
        return signInRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    @Transactional
    public CartItemResponseDto increaseQuantity(Long userId, Integer cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException(NOTFOUNDMESSAGE));
        if (!item.getUserId().equals(userId)) {
            throw new IllegalArgumentException("This item does not belong to you");
        }
        item.setQuantity(item.getQuantity() + 1);
        cartItemRepository.save(item);
        log.info("Cart item: {} quantity increased to {}", item.getId(),  item.getQuantity());
        return cartItemMapper.toResponseDto(item);
    }

    @Transactional
    public CartItemResponseDto decreaseQuantity(Long userId, Integer cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException(NOTFOUNDMESSAGE));
        if (!item.getUserId().equals(userId)) {
            throw new IllegalArgumentException("This item does not belong to you");
        }
        if (item.getQuantity() <= 1) {
            cartItemRepository.delete(item);
            log.info("Cart item {} removed (quantity was 1)", cartItemId);
            return cartItemMapper.toResponseDto(item);
        }
        item.setQuantity(item.getQuantity() - 1);
        cartItemRepository.save(item);
        log.info("Cart item: {} quantity decreased to {}", item.getId(), item.getQuantity());
        return cartItemMapper.toResponseDto(item);
    }

    @Transactional
    public CartSummaryDto getCartSummary(Long userId, String couponCode) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

        float subtotal = cartItems.stream()
                .map(item -> item.getQuantity() * item.getShopItem().getPrice())
                .reduce(0f, Float::sum);

        float discount = 0f;
        String appliedCouponCode = null;

        if (couponCode != null && !couponCode.isEmpty()) {
            Optional<Coupon> coupon = couponRepository.findByCode(couponCode);
            if (coupon.isPresent()) {
                discount = subtotal * coupon.get().getDiscount();
                appliedCouponCode = couponCode;
            }
        }
        float total = subtotal - discount;
        return new CartSummaryDto(subtotal, discount, total, appliedCouponCode);
    }
}
