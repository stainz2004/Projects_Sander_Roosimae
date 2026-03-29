package com.example.veebilehekala.service;

import com.example.veebilehekala.controller.shopitem.ShopItemDto;
import com.example.veebilehekala.controller.shopitem.ShopItemResponseDto;
import com.example.veebilehekala.entity.Category;
import com.example.veebilehekala.entity.ShopItem;
import com.example.veebilehekala.mapper.ShopItemMapper;
import com.example.veebilehekala.mapper.ShopItemMapperImpl;
import com.example.veebilehekala.repository.CategoryRepository;
import com.example.veebilehekala.repository.ShopItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ShopItemServiceTest {

    @Spy
    private final ShopItemMapper shopItemMapper = new ShopItemMapperImpl();
    @Mock
    private  ShopItemRepository shopItemRepository;
    @Mock
    private  CategoryRepository categoryRepository;

    @InjectMocks
    private ShopItemService shopItemService;

    private ShopItemResponseDto shopItemResponseDto;
    private ShopItemDto shopItemDto;
    private Category category;
    private ShopItem shopitem;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1)
                .name("Test Category")
                .build();
        shopItemResponseDto = ShopItemResponseDto.builder()
                .id(1)
                .name("Test Item")
                .description("This is a test item")
                .price(9.99f)
                .categoryId(1)
                .categoryName("Test Category")
                .productUrl("http://example.com/test-item")
                .build();
        shopItemDto = ShopItemDto.builder()
                .id(1)
                .name("Test Item")
                .description("This is a test item")
                .price(9.99f)
                .categoryId(1)
                .productUrl("http://example.com/test-item")
                .build();
        shopitem = ShopItem.builder()
                .id(1)
                .name("Test Item")
                .description("This is a test item")
                .price(9.99f)
                .category(category)
                .productUrl("http://example.com/test-item")
                .build();
    }

    @Test
    void getShopItemByIdWorks() {
        given(shopItemRepository.findById(1)).willReturn(Optional.of(shopitem));

        ShopItemResponseDto result = shopItemService.getShopItemById(1);

        assertEquals(shopItemResponseDto, result);
    }

    @Test
    void getAllShopItemsWorks() {
        given(shopItemRepository.findAll(any(Specification.class), any(Sort.class)))
                .willReturn(List.of(shopitem));

        List<ShopItemResponseDto> result = shopItemService.getAllShopItems(0.1, 1000.1, List.of(1), "price_asc");

        assertEquals(List.of(shopItemResponseDto), result);
    }

    @Test
    void deleteShopItemDeletes() {
        given(shopItemRepository.existsById(1)).willReturn(true);

        shopItemService.deleteShopItem(1);

        verify(shopItemRepository).deleteById(1);
    }

    @Test
    void deleteShopItemDoesNotExist() {
        given(shopItemRepository.existsById(1)).willReturn(false);

        assertThrows(EntityNotFoundException.class, () -> shopItemService.deleteShopItem(1));
    }

    @Test
    void createShopItemCreates() {
        given(categoryRepository.findById(1))
                .willReturn(Optional.of(category));
        given(shopItemRepository.save(any(ShopItem.class)))
                .willReturn(shopitem);

        ShopItemResponseDto result = shopItemService.createShopItem(shopItemDto);

        assertEquals(shopItemResponseDto, result);
    }

    @Test
    void updateShopItemUpdates() {
        given(shopItemRepository.findById(1)).willReturn(Optional.of(shopitem));
        given(categoryRepository.findById(1)).willReturn(Optional.of(category));
        given(shopItemRepository.save(any(ShopItem.class))).willReturn(shopitem);

        ShopItemResponseDto result = shopItemService.updateShopItem(1, shopItemDto);

        assertEquals(shopItemResponseDto, result);
    }

}
