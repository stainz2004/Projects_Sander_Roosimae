package com.example.veebilehekala.service;

import com.example.veebilehekala.controller.review.ReviewDto;
import com.example.veebilehekala.controller.review.ReviewResponseDto;
import com.example.veebilehekala.entity.Category;
import com.example.veebilehekala.entity.Customer;
import com.example.veebilehekala.entity.Review;
import com.example.veebilehekala.entity.Role;
import com.example.veebilehekala.entity.ShopItem;
import com.example.veebilehekala.exception.ApplicationException;
import com.example.veebilehekala.mapper.ReviewMapper;
import com.example.veebilehekala.mapper.ReviewMapperImpl;
import com.example.veebilehekala.repository.ReviewRepository;
import com.example.veebilehekala.repository.ShopItemRepository;
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
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Spy
    private ReviewMapper reviewMapper = new ReviewMapperImpl();
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private SignInRepository signInRepository;
    @Mock
    private ShopItemRepository shopItemRepository;
    @Mock
    private Authentication authentication;


    @InjectMocks
    private ReviewService reviewService;

    private ReviewResponseDto reviewResponseDto;
    private ReviewDto reviewDto;
    private Review review;
    private Customer customer;
    private ShopItem shopItem;

    @BeforeEach
    void setUp() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Role role = Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();
        Category category = Category.builder()
                .id(1)
                .name("Test Category")
                .build();
        customer = Customer.builder()
                .id(1L)
                .name("Test User")
                .username("testuser")
                .email("testuser@gmail.com")
                .password("password")
                .roles(Set.of(role))
                .build();
        shopItem = ShopItem.builder()
                .id(1)
                .name("Test Item")
                .description("This is a test item")
                .price(10.0f)
                .category(category)
                .productUrl("http://example.com/product/1")
                .build();
        review = Review.builder()
                .id(1)
                .customer(customer)
                .shopItem(shopItem)
                .rating(5)
                .comment("Great product!")
                .createdAt(localDateTime)
                .build();
        reviewDto = ReviewDto.builder()
                .customerName("Test User")
                .shopItemId(1)
                .rating(5)
                .comment("Great product!")
                .build();
        reviewResponseDto = ReviewResponseDto.builder()
                .id(1)
                .customerName("Test User")
                .shopItemId(1)
                .shopItemName("Test Item")
                .rating(5)
                .comment("Great product!")
                .createdAt(localDateTime)
                .build();
    }

    @Test
    void createReviewWorks() {
        given(reviewRepository.findByCustomerUsernameIgnoreCaseAndShopItemId("Test User", 1))
                .willReturn(Optional.empty());
        given(signInRepository.findByUsernameIgnoreCase("Test User"))
                .willReturn(Optional.of(customer));
        given(shopItemRepository.findById(1)).willReturn(Optional.of(shopItem));
        given(reviewRepository.save(any(Review.class))).willReturn(review);
        given(reviewMapper.mapToDTO(any(Review.class))).willReturn(reviewResponseDto);
        given(authentication.getName()).willReturn("Test User");

        ReviewResponseDto createdReview = reviewService.createReview(reviewDto, authentication);

        assertEquals(reviewResponseDto, createdReview);
    }

    @Test
    void createReviewCustomerAlreadyReviewedDoesNotWork() {
        assertThrows(ApplicationException.class, () -> {
            reviewService.createReview(reviewDto, authentication);
        });
    }

    @Test
    void getAllReviewsWorks() {
        given(reviewRepository.findAll()).willReturn(java.util.List.of(review));

        java.util.List<ReviewResponseDto> reviews = reviewService.getAllReviews();

        assertEquals(1, reviews.size());
        assertEquals(reviewResponseDto, reviews.get(0));
    }

    @Test
    void getReviewAvgByShopItemWorks() {
        given(reviewRepository.findAverageRatingByShopItemId(1)).willReturn(4.5);

        double avgRating = reviewService.getReviewAvgByShopItem(1);

        assertEquals(4.5, avgRating, 0.01);
    }

    @Test
    void getCountByShopItemWorks() {
        given(reviewRepository.countByShopItemId(1)).willReturn(10L);

        long count = reviewService.getCountByShopItem(1);

        assertEquals(10L, count);
    }

    @Test
    void getReviewsByShopItemWorks() {
        given(reviewRepository.findByShopItemId(1)).willReturn(java.util.List.of(review));
        given(reviewMapper.mapToDTO(any(Review.class))).willReturn(reviewResponseDto);

        java.util.List<ReviewResponseDto> reviews = reviewService.getReviewsByShopItem(1);

        assertEquals(1, reviews.size());
        assertEquals(reviewResponseDto, reviews.get(0));
    }

    @Test
    void deleteReviewWorks() {
        given(reviewRepository.existsById(1)).willReturn(true);

        reviewService.deleteReview(1);

        // No exception means success
        verify(reviewRepository).deleteById(1);
    }

    @Test
    void deleteReviewNotFound() {
        given(reviewRepository.existsById(1)).willReturn(false);

        assertThrows(ApplicationException.class, () -> reviewService.deleteReview(1));
    }

    @Test
    void getReviewsByCustomerNameWorks() {
        given(reviewRepository.findByCustomerUsernameIgnoreCase("Test User")).willReturn(java.util.List.of(review));
        given(reviewMapper.mapToDTO(any(Review.class))).willReturn(reviewResponseDto);
        List<ReviewResponseDto> reviews = reviewService.getReviewsByCustomerName("Test User");
        assertEquals(1, reviews.size());
        assertEquals(reviewResponseDto, reviews.get(0));
    }
}
