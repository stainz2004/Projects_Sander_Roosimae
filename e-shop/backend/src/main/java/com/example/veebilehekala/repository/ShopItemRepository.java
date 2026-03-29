package com.example.veebilehekala.repository;

import com.example.veebilehekala.entity.ShopItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShopItemRepository extends JpaRepository<ShopItem, Integer>,
        JpaSpecificationExecutor<ShopItem> {
}
