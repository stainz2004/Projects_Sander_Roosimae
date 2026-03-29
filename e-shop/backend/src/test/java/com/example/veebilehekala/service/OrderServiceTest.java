package com.example.veebilehekala.service;

import com.example.veebilehekala.controller.order.CreateOrderDto;
import com.example.veebilehekala.controller.order.OrderResponseDto;
import com.example.veebilehekala.controller.orderitem.OrderItemResponseDto;
import com.example.veebilehekala.entity.CartItem;
import com.example.veebilehekala.entity.Category;
import com.example.veebilehekala.entity.Coupon;
import com.example.veebilehekala.entity.Customer;
import com.example.veebilehekala.entity.Order;
import com.example.veebilehekala.entity.OrderItem;
import com.example.veebilehekala.entity.Role;
import com.example.veebilehekala.entity.ShopItem;
import com.example.veebilehekala.mapper.OrderMapper;
import com.example.veebilehekala.mapper.OrderMapperImpl;
import com.example.veebilehekala.repository.CartItemRepository;
import com.example.veebilehekala.repository.CouponRepository;
import com.example.veebilehekala.repository.OrderRepository;
import com.example.veebilehekala.repository.SignInRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private  OrderRepository ordersRepository;
    @Mock
    private  SignInRepository signInRepository;
    @Mock
    private  CartItemRepository cartItemRepository;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private  JwtTokenService jwtTokenService;

    @Spy
    private  OrderMapper orderMapper = new OrderMapperImpl();

    @InjectMocks
    private OrderService orderService;

    private OrderResponseDto orderResponseDto;
    private CreateOrderDto createOrderDto;
    private Customer customer;
    private OrderItem orderItem;
    private Order order;
    private Coupon coupon;
    private CartItem cartItem;
    private ShopItem shopItem;
    private OrderItemResponseDto orderItemResponseDto;
    private Order order2;


    @BeforeEach
    void setUp() {
        LocalDateTime localDateTime = LocalDateTime.now();
        coupon = Coupon.builder()
                .id(1)
                .code("TESTCOUPON")
                .discount(0.1f)
                .build();
        Role role = Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();
        orderItemResponseDto = OrderItemResponseDto.builder()
                .id(1)
                .shopItemId(1)
                .productName("test item")
                .productCategory("test category")
                .unitPrice(10.0)
                .quantity(2)
                .build();
        orderResponseDto = OrderResponseDto
                .builder()
                .id(1)
                .customerName("testuser")
                .price(100f)
                .couponId(1)
                .timestamp(localDateTime)
                .items(List.of(orderItemResponseDto))
                .build();
        Category category = Category.builder()
                .id(1)
                .name("test category")
                .build();
        shopItem = ShopItem.builder()
                .id(1)
                .name("test item")
                .description("This is a test item")
                .price(10.0f)
                .category(category)
                .productUrl("http://example.com/product/1")
                .build();
        createOrderDto = CreateOrderDto.builder().couponId(1).build();
        customer = Customer.builder()
                .id(1L)
                .name("Test User")
                .username("testuser")
                .email("testuser@gmail.com")
                .password("password")
                .roles(Set.of(role))
                .build();

        orderItem = OrderItem.builder()
                .id(1)
                .order(order)
                .shopItem(shopItem)
                .productName("test item")
                .productCategory("test category")
                .unitPrice(10.0)
                .quantity(2)
                .build();
        order = Order.builder()
                .id(1)
                .customer(customer)
                .totalPrice(100f)
                .coupon(coupon)
                .createdAt(localDateTime)
                .items(List.of(orderItem))
                .build();
        order2 = Order.builder()
                .id(2)
                .customer(customer)
                .totalPrice(100f)
                .coupon(coupon)
                .createdAt(localDateTime)
                .items(List.of(orderItem))
                .build();
        cartItem = CartItem.builder()
                .id(1)
                .shopItem(shopItem)
                .userId(1L)
                .quantity(2)
                .build();
    }

    @Test
    void testCreateOrderWorks() {
        given(signInRepository.findByUsernameIgnoreCase("testuser"))
                .willReturn(Optional.of(customer));
        given(couponRepository.findById(1))
                .willReturn(Optional.of(coupon));
        given(cartItemRepository.findByUserId(1L))
                .willReturn(List.of(cartItem));
        given(ordersRepository.save(any(Order.class)))
                .willReturn(order);

        OrderResponseDto createdOrder = orderService.createOrder("testuser", createOrderDto);

        assertEquals(orderResponseDto, createdOrder);
    }

    @Test
    void testCreateOrderCartItemsEmpty() {
        given(signInRepository.findByUsernameIgnoreCase("testuser"))
                .willReturn(Optional.of(customer));
        given(couponRepository.findById(1))
                .willReturn(Optional.of(coupon));
        given(cartItemRepository.findByUserId(1L))
                .willReturn(List.of());


        assertThrows(IllegalStateException.class, () -> {
            orderService.createOrder("testuser", createOrderDto);
        });
    }

    @Test
    void testGetOrderByIdWorks() {
        given(ordersRepository.findById(1)).willReturn(Optional.of(order));

        OrderResponseDto order1 = orderService.getOrderById(1);

        assertEquals(orderResponseDto, order1);
    }

    @Test
    void testGetOrderByCustomerWorks() {
        Authentication authentication = mock(Authentication.class);

        given(authentication.getName()).willReturn("testuser");
        given(signInRepository.findByUsernameIgnoreCase("testuser"))
                .willReturn(Optional.of(customer));
        given(ordersRepository.findAllByCustomerId(1L))
                .willReturn(List.of(order, order2));

        List<OrderResponseDto> orders = orderService.getOrdersByCustomer(authentication);

        assertEquals(2, orders.size());
        assertEquals(orderResponseDto, orders.get(0));
    }
}
