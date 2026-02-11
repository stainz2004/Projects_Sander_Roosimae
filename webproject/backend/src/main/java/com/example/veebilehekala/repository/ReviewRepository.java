package com.example.veebilehekala.repository;

import com.example.veebilehekala.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByShopItemId(Integer shopItemId);
    // So that there's only one review per customer on a product
    Optional<Review> findByCustomerUsernameIgnoreCaseAndShopItemId(String customerName, Integer shopItemId);
    @Query("""
    SELECT COALESCE(AVG(r.rating), 0)
    FROM Review r
    WHERE r.shopItem.id = :shopItemId
""")
    double findAverageRatingByShopItemId(@Param("shopItemId") Integer shopItemId);
    long countByShopItemId(Integer shopItemId);
    List<Review> findByCustomerUsernameIgnoreCase(String customerName);
}