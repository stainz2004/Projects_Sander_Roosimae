package com.example.veebilehekala.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ShopItemControllerTest extends AbstractIntegrationTest{

    @Autowired
    private MockMvc mvc;

    @Test
    void getShopItemByIdWorks() throws Exception {
        mvc.perform(get("/api/public/shop/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getShopItemsWorks() throws Exception {
        mvc.perform(get("/api/public/shop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void createShopItemWorks() throws Exception {
        mvc.perform(post("/api/admin/items")
                .with(user("admin").roles("ADMIN"))
                .contentType("application/json")
                .content("""
                        {
                            "name": "New Item",
                            "description": "This is a new item",
                            "price": 20.00,
                            "categoryId": 1,
                            "productUrl": "https://example.com/new-item"
                        }
                        """)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void deleteShopItemWorks() throws Exception {
       mvc.perform(delete("/api/admin/items/1")
               .with(user("admin").roles("ADMIN")))

               .andExpect(status().isNoContent());
       mvc.perform(get("/api/public/shop/1"))
               .andExpect(status().isNotFound());
    }

    @Test
    void updateShopItemWorks() throws Exception {
        mvc.perform(put("/api/admin/items/1")
                .with(user("admin").roles("ADMIN"))
                .contentType("application/json")
                .content("""
                        {
                            "name": "Updated Item",
                            "description": "This is an updated item",
                            "price": 25.00,
                            "categoryId": 1,
                            "productUrl": "https://example.com/updated-item"
                        }
                        """)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Item"))
                .andExpect(jsonPath("$.price").value(25.00));
    }

    @Test
    void getSignImageUploadWorks() throws Exception {
        mvc.perform(get("/api/admin/image/sign")
                .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }
}
