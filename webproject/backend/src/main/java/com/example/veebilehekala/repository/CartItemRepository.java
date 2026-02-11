package com.example.veebilehekala.repository;

import com.example.veebilehekala.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    List<CartItem> findByUserId(Long userId);
    Optional<CartItem> findByUserIdAndShopItemId(Long userId, Integer shopItemId);
    void deleteByUserId(Long userId);
}
