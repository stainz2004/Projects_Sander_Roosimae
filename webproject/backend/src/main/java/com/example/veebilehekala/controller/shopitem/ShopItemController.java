package com.example.veebilehekala.controller.shopitem;

import com.example.veebilehekala.service.ShopItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
@Tag(name = "Shop Items", description = "Shop Items API")
public class ShopItemController {

    private final ShopItemService shopItemService;

    @GetMapping("public/shop/{id}")
    @Operation(summary = "Get shop item by id", description = "Get shop item by id")
    @ApiResponse(responseCode = "200", description = "Found and return shop item")
    public ShopItemResponseDto getShopItemById(@PathVariable Integer id) {
        return shopItemService.getShopItemById(id);
    }

    @GetMapping("public/shop")
    @Operation(summary = "Get all shop items", description = "Get all shop items")
    @ApiResponse(responseCode = "200", description = "Found and return shop items")
    public List<ShopItemResponseDto> getShopItems(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(defaultValue = "price_asc") String sort) {
        return shopItemService.getAllShopItems(minPrice, maxPrice, categories, sort);
    }

    @PostMapping("admin/items")
    @Operation(summary = "Create shop item", description = "Create new shop item")
    @ApiResponse(responseCode = "201", description = "Shop item created")
    @ResponseStatus(HttpStatus.CREATED)
    public ShopItemResponseDto createShopItem(@Valid @RequestBody ShopItemDto shopItemDto) {
        return shopItemService.createShopItem(shopItemDto);
    }

    @DeleteMapping("admin/items/{id}")
    @Operation(summary = "Delete shop item", description = "Delete shop item")
    @ApiResponse(responseCode = "204", description = "Shop item deleted")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Integer id) {
        shopItemService.deleteShopItem(id);
    }

    @PutMapping("admin/items/{id}")
    @Operation(summary = "Update shop item", description = "Update existing shop item by id")
    @ApiResponse(responseCode = "200", description = "Shop item updated")
    public ShopItemResponseDto updateShopItem(
            @PathVariable Integer id,
            @Valid @RequestBody ShopItemDto shopItemDto) {
        return shopItemService.updateShopItem(id, shopItemDto);
    }

    @GetMapping("admin/image/sign")
    @Operation(summary = "Key for API", description = "Information for image upload")
    @ApiResponse(responseCode = "200", description = "returned keys for image upload")
    public Map<String, Object> signImageUpload() {
        return shopItemService.signImageUpload();
    }
}
