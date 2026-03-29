package com.example.veebilehekala.service;

import com.example.veebilehekala.controller.shopitem.ShopItemDto;
import com.example.veebilehekala.controller.shopitem.ShopItemResponseDto;
import com.example.veebilehekala.entity.Category;
import com.example.veebilehekala.entity.ShopItem;
import com.example.veebilehekala.mapper.ShopItemMapper;
import com.example.veebilehekala.repository.CategoryRepository;
import com.example.veebilehekala.repository.ShopItemRepository;
import com.example.veebilehekala.repository.ShopItemSpecifications;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopItemService {
    private final ShopItemMapper shopItemMapper;
    private final ShopItemRepository shopItemRepository;
    private final CategoryRepository categoryRepository;

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    public ShopItemResponseDto getShopItemById(Integer id) {
        ShopItem shopItem = shopItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "ShopItem not found with id " + id
                ));
        return shopItemMapper.mapToDto(shopItem);
    }

    public List<ShopItemResponseDto> getAllShopItems(
            Double min,
            Double max,
            List<Integer> categoryIds,
            String sort
    ) {
        Sort sortSpec = switch (sort == null ? "" : sort) {
            case "name_asc"  -> Sort.by("name").ascending();
            case "name_desc" -> Sort.by("name").descending();
            case "price_desc"-> Sort.by("price").descending();
            default          -> Sort.by("price").ascending();
        };

        return shopItemRepository
                .findAll(
                        ShopItemSpecifications.withFilters(min, max, categoryIds),
                        sortSpec
                )
                .stream()
                .map(shopItemMapper::mapToDto)
                .toList();
    }

    public void deleteShopItem(Integer id) {
        if (!shopItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Item with id " + id + " does not exist");
        }
        shopItemRepository.deleteById(id);
        log.info("Shop item deleted: {}", id);
    }

    public ShopItemResponseDto createShopItem(ShopItemDto shopItemDto) {
        Category category = categoryRepository.findById(shopItemDto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + shopItemDto.getCategoryId()));

        ShopItem shopItem = shopItemMapper.toEntity(shopItemDto, category);
        ShopItem saved = shopItemRepository.save(shopItem);
        log.info("Shop item created: {}", saved.getId());
        return shopItemMapper.mapToDto(saved);
    }

    public ShopItemResponseDto updateShopItem(Integer id, ShopItemDto shopItemDto) {
        ShopItem shopItem = shopItemRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Category category = categoryRepository.findById(shopItemDto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Category with id " + shopItemDto.getCategoryId() + " not found"));

        shopItem = shopItemMapper.updateEntity(shopItemDto, category, shopItem);
        ShopItem savedShopItem = shopItemRepository.save(shopItem);
        log.info("Shop item updated: {}", savedShopItem.getId());
        return shopItemMapper.mapToDto(savedShopItem);
    }

    public Map<String, Object> signImageUpload() {
        long timestamp = System.currentTimeMillis() / 1000;

        String toSign = "timestamp=" + timestamp;
        String signature = DigestUtils.sha1Hex(toSign + apiSecret);

        return Map.of(
                "cloudName", cloudName,
                "apiKey", apiKey,
                "timestamp", timestamp,
                "signature", signature
        );
    }
}
