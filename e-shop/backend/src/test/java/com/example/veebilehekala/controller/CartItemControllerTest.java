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
class CartItemControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void getCartWorks() throws Exception {
        mvc.perform(get("/api/cart")
                        .with(user("johndoe").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Sample Item"));
    }

    @Test
    void addToCartWorks() throws Exception {
        mvc.perform(post("/api/cart")
                        .with(user("testuser").roles("USER"))
                        .contentType("application/json")
                        .content("""
                                {
                                  "shopItemId": 1,
                                  "quantity": 2
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Sample Item"))
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    void deleteCartItemWorks() throws Exception {
        mvc.perform(delete("/api/cart/1")
                        .with(user("johndoe").roles("USER")))
                .andExpect(status().isNoContent());
    }

    @Test
    void clearCartWorks() throws Exception {
        mvc.perform(delete("/api/cart")
                        .with(user("johndoe").roles("USER")))
                .andExpect(status().isNoContent());
    }

    @Test
    void increaseQuantityWorks() throws Exception {
        mvc.perform(put("/api/cart/1/increase")
                        .with(user("johndoe").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(3));
    }

    @Test
    void decreaseQuantityWorks() throws Exception {
        mvc.perform(put("/api/cart/1/decrease")
                        .with(user("johndoe").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(1));
    }

    @Test
    void getCartSummaryWorks() throws Exception {
        mvc.perform(get("/api/cart/summary")
                        .with(user("johndoe").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subtotal").value(100.0));
    }
}
