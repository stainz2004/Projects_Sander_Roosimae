package com.example.veebilehekala.service;

import com.example.veebilehekala.controller.review.ReviewDto;
import com.example.veebilehekala.controller.review.ReviewResponseDto;
import com.example.veebilehekala.entity.Customer;
import com.example.veebilehekala.entity.Review;
import com.example.veebilehekala.entity.ShopItem;
import com.example.veebilehekala.exception.ApplicationException;
import com.example.veebilehekala.mapper.ReviewMapper;
import com.example.veebilehekala.repository.ReviewRepository;
import com.example.veebilehekala.repository.ShopItemRepository;
import com.example.veebilehekala.repository.SignInRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewMapper reviewMapper;
    private final ReviewRepository reviewRepository;
    private final SignInRepository signInRepository;
    private final ShopItemRepository shopItemRepository;

    public ReviewResponseDto createReview(ReviewDto request, Authentication authentication) {

        // 1. Load referenced entities
        Customer customer = signInRepository.findByUsernameIgnoreCase(request.getCustomerName())
                .orElseThrow(() -> new ApplicationException("Customer not found", 404));

        // 2. Check for duplicates (one review per customer per product)
        Optional<Review> exists = reviewRepository
                .findByCustomerUsernameIgnoreCaseAndShopItemId(authentication.getName(), request.getShopItemId());

        if (exists.isPresent()) {
            throw new ApplicationException("You have already reviewed this product.", 409);
        }

        ShopItem shopItem = shopItemRepository.findById(request.getShopItemId())
                .orElseThrow(() -> new RuntimeException("Shop item not found"));

        Review review = reviewMapper.toEntity(request, customer, shopItem);

        reviewRepository.save(review);
        log.info("Review created: {} by customer: {}", review.getId(), customer.getName());

        return reviewMapper.mapToDTO(review);
    }

    public List<ReviewResponseDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(reviewGet -> new ReviewResponseDto(reviewGet.getId(), reviewGet.getCustomer().getName(),
                        reviewGet.getShopItem().getId(), reviewGet.getShopItem().getName(), reviewGet.getRating(),
                        reviewGet.getComment(), reviewGet.getCreatedAt()))
                .toList();
    }

    public double getReviewAvgByShopItem(Integer shopItemId) {
        return reviewRepository.findAverageRatingByShopItemId(shopItemId);
    }

    public long getCountByShopItem(Integer shopItemId) {
        return reviewRepository.countByShopItemId(shopItemId);
    }


    public List<ReviewResponseDto> getReviewsByShopItem(Integer shopItemId) {
        return reviewRepository.findByShopItemId(shopItemId)
                .stream()
                .map(reviewMapper::mapToDTO)
                .toList();
    }

    public void deleteReview(Integer id) {
        if (!reviewRepository.existsById(id)) {
            throw new ApplicationException("Review not found", 404);
        }
        reviewRepository.deleteById(id);
        log.info("Review deleted: {}", id);
    }

    public List<ReviewResponseDto> getReviewsByCustomerName(String username) {
        log.info("username {}", username);
        return reviewRepository
                .findByCustomerUsernameIgnoreCase(username)
                .stream()
                .map(reviewMapper::mapToDTO)
                .toList();
    }
}
