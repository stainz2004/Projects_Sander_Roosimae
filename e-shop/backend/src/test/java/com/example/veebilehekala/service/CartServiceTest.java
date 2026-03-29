package com.example.veebilehekala.service;

import com.example.veebilehekala.controller.cartitem.AddToCartDto;
import com.example.veebilehekala.controller.cartitem.CartItemResponseDto;
import com.example.veebilehekala.entity.CartItem;
import com.example.veebilehekala.entity.Category;
import com.example.veebilehekala.entity.Customer;
import com.example.veebilehekala.entity.Role;
import com.example.veebilehekala.entity.ShopItem;
import com.example.veebilehekala.mapper.CartItemMapperImpl;
import com.example.veebilehekala.repository.CartItemRepository;
import com.example.veebilehekala.repository.ShopItemRepository;
import com.example.veebilehekala.repository.SignInRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ShopItemRepository shopItemRepository;
    @Mock
    private SignInRepository signInRepository;

    @Spy
    private CartItemMapperImpl cartItemMapper = new CartItemMapperImpl();

    @InjectMocks
    private CartService cartService;

    private ShopItem shopItem;
    private CartItem cartItem;
    private AddToCartDto addToCartDto;
    private CartItemResponseDto cartItemResponseDto;
    private CartItemResponseDto cartItemResponseDto2;
    private Long userId;
    private Customer customer;

    @BeforeEach
    void setUp() {
        userId = 1L;
        Role role = Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();
        customer = Customer.builder()
                .id(1L)
                .name("Test User")
                .username("testuser")
                .email("testuser@gmail.com")
                .password("password")
                .roles(Set.of(role))
                .build();
        Category category = Category.builder()
                .id(1)
                .name("Test Category")
                .build();
        shopItem = ShopItem.builder()
                .id(1)
                .name("Test Item")
                .description("This is a test item")
                .price(10.0f)
                .category(category)
                .productUrl("http://example.com/product/1")
                .build();
        cartItem = CartItem.builder()
                .id(1)
                .shopItem(shopItem)
                .userId(1L)
                .quantity(2)
                .build();
        addToCartDto = AddToCartDto.builder()
                .shopItemId(1)
                .quantity(2)
                .build();
        cartItemResponseDto = CartItemResponseDto.builder()
                .id(1)
                .userId(userId)
                .shopItemId(1)
                .name("Test Item")
                .description("This is a test item")
                .price(10.0f)
                .quantity(2)
                .totalPrice(20.0f)
                .productUrl("http://example.com/product/1")
                .build();
        cartItemResponseDto2 = CartItemResponseDto.builder()
                .id(1)
                .userId(userId)
                .shopItemId(1)
                .name("Test Item")
                .description("This is a test item")
                .price(10.0f)
                .quantity(4)
                .totalPrice(40.0f)
                .productUrl("http://example.com/product/1")
                .build();
    }


    @Test
    void testAddToCartAlreadyExists() {
        // given
        given(shopItemRepository.findById(1)).willReturn(Optional.of(shopItem));
        given(cartItemRepository.findByUserIdAndShopItemId(1L, 1)).willReturn(Optional.of(cartItem));
        given(cartItemRepository.save(any(CartItem.class))).willReturn(cartItem);
        // when
        CartItemResponseDto cartItemResponse = cartService.addToCart(userId, addToCartDto);
        // then
        assertEquals(cartItemResponseDto2, cartItemResponse);
    }

    @Test
    void testAddToCartDoesntExistAlready() {
        // given
        given(shopItemRepository.findById(1)).willReturn(Optional.of(shopItem));
        given(cartItemRepository.findByUserIdAndShopItemId(1L, 1)).willReturn(Optional.empty());
        given(cartItemRepository.save(any(CartItem.class))).willReturn(cartItem);
        // when
        CartItemResponseDto cartItemResponse = cartService.addToCart(userId, addToCartDto);
        // then
        assertEquals(cartItemResponseDto, cartItemResponse);
    }

    @Test
    void getCartByUserIdWorks() {
        // given
        given(cartItemRepository.findByUserId(1L)).willReturn(java.util.List.of(cartItem));
        // when
        List<CartItemResponseDto> cartItemResponseDtos = cartService.getCartByUserId(1L);
        // then
        assertEquals(List.of(cartItemResponseDto), cartItemResponseDtos);
    }

    @Test
    void deleteCartItemWorks() {
        // given
        given(cartItemRepository.findById(1)).willReturn(Optional.of(cartItem));
        // when
        cartService.deleteCartItem(1L, 1);
        // then
        verify(cartItemRepository).delete(cartItem);
    }

    @Test
    void deleteCartItemDoesntBelongToUser() {
        // given
        given(cartItemRepository.findById(1)).willReturn(Optional.of(cartItem));
        // when
        assertThrows(IllegalArgumentException.class, () -> cartService.deleteCartItem(2L, 1));
    }

    @Test
    void clearCartWorks() {
        // given

        // when
        cartService.clearCart(1L);
        // then
        verify(cartItemRepository).deleteByUserId(userId);
    }

    @Test
    void getUserIdFromAuthWorks() {
        // given
        given(signInRepository.findByUsernameIgnoreCase("testuser")).willReturn(Optional.of(customer));
        // when
        Long id = cartService.getUserIdFromAuth(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("testuser", "password"));
        // then
        assertEquals(1L, id);
    }
}
