package com.example.veebilehekala.controller.cartitem;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.veebilehekala.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
@Tag(name = "Cart", description = "Cart API")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Get cart", description = "Get cart by user id")
    @ApiResponse(responseCode = "200", description = "Found and return cart items")
    @GetMapping("cart")
    public ResponseEntity<Object> getCart(Authentication auth) {
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in");
        }
        Long userId = cartService.getUserIdFromAuth(auth);
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("cart")
    @Operation(summary = "Add to cart", description = "Add item to cart")
    @ApiResponse(responseCode = "201", description = "Item added to cart")
    @ResponseStatus(HttpStatus.CREATED)
    public CartItemResponseDto addToCart(@RequestBody AddToCartDto dto, Authentication auth) {
        Long userId = cartService.getUserIdFromAuth(auth);
        return cartService.addToCart(userId, dto);
    }

    @DeleteMapping("cart/{id}")
    @Operation(summary = "Delete cart item", description = "Delete item from cart")
    @ApiResponse(responseCode = "204", description = "Item deleted from cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable Integer id, Authentication auth) {
        Long userId = cartService.getUserIdFromAuth(auth);
        cartService.deleteCartItem(userId, id);
    }

    @DeleteMapping("cart")
    @Operation(summary = "Clear cart", description = "Clear all items from cart")
    @ApiResponse(responseCode = "204", description = "Cart cleared")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(Authentication auth) {
        Long userId = cartService.getUserIdFromAuth(auth);
        cartService.clearCart(userId);
    }

    @PutMapping("cart/{id}/increase")
    @Operation(summary = "Increase quantity", description = "Increase cart item quantity by 1")
    @ApiResponse(responseCode = "200", description = "Quantity increased")
    public CartItemResponseDto increaseQuantity(@PathVariable Integer id, Authentication auth) {
        Long userId = cartService.getUserIdFromAuth(auth);
        return cartService.increaseQuantity(userId, id);
    }

    @PutMapping("cart/{id}/decrease")
    @Operation(summary = "Decrease quantity", description = "Decrease cart item quantity by 1")
    @ApiResponse(responseCode = "200", description = "Quantity decreased")
    public CartItemResponseDto decreaseQuantity(@PathVariable Integer id, Authentication auth) {
        Long userId = cartService.getUserIdFromAuth(auth);
        return cartService.decreaseQuantity(userId, id);
    }

    @GetMapping("cart/summary")
    @Operation(summary = "Get cart summary with coupon", description = "Calculate cart total with applied coupon")
    @ApiResponse(responseCode = "200", description = "Cart summary")
    public ResponseEntity<CartSummaryDto> getCartSummary(
            @RequestParam(required = false) String couponCode,
            Authentication auth) {
        Long userId = cartService.getUserIdFromAuth(auth);
        return ResponseEntity.ok(cartService.getCartSummary(userId, couponCode));
    }
}
