package com.example.veebilehekala.controller.review;

import com.example.veebilehekala.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/public/reviews")
@Tag(name = "Reviews", description = "Reviews API")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create review", description = "Create new review")
    @ApiResponse(responseCode = "201", description = "Review created")
    public ReviewResponseDto createReview(@Valid @RequestBody ReviewDto request, Authentication authentication) {
        return reviewService.createReview(request, authentication);
    }

    @GetMapping
    @Operation(summary = "Get all reviews", description = "Get all reviews")
    @ApiResponse(responseCode = "200", description = "Found and return reviews")
    public List<ReviewResponseDto> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get my reviews",
            description = "Returns all reviews written by the authenticated user"
    )

    @ApiResponse(responseCode = "200", description = "Found and return reviews")
    public List<ReviewResponseDto> getMyReviews(Authentication authentication) {
        String username = authentication.getName();
        return reviewService.getReviewsByCustomerName(username);
    }

    @GetMapping("/items/{shopItemId}")
    @Operation( summary = "Get all reviews for a shop item",
            description = "Returns a list of all reviews written for the specified shop item.")
    @ApiResponse(responseCode = "200", description = "Found and return reviews")
    public List<ReviewResponseDto> getReviewsByShopItem( @PathVariable(required = true) Integer shopItemId) {
        return reviewService.getReviewsByShopItem(shopItemId);
    }

    @GetMapping("/items/{shopItemId}/Avg")
    @Operation(summary = "Get average rating by shop item", description = "Calculates and returns average rating for shop item")
    @ApiResponse(responseCode = "200", description = "Average rating returned")
    public double getReviewAvgByShopItem(@PathVariable Integer shopItemId) {
        return reviewService.getReviewAvgByShopItem(shopItemId);
    }

    @GetMapping("/items/{shopItemId}/Amount")
    @Operation(summary = "Get amount of reviews by shop item", description = "Returns how many reviews this shop item has.")
    @ApiResponse(responseCode = "200", description = "Review count returned")
    public long getReviewAmountByShopItem(@PathVariable Integer shopItemId) {
        return reviewService.getCountByShopItem(shopItemId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete review", description = "Delete review")
    @ApiResponse(responseCode = "204", description = "Review deleted")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable Integer id) {
        reviewService.deleteReview(id);
    }
}
