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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReviewControllerTest extends AbstractIntegrationTest{

    @Autowired
    private MockMvc mvc;

    @Test
    void createReviewWorks() throws Exception {
        mvc.perform(post("/api/public/reviews")
                .with(user("johndoe").roles("USER"))
                        .contentType("application/json")
                        .content("""
                                {
                                    "customerName": "johndoe",
                                    "shopItemId": 1,
                                    "rating": 5,
                                    "comment": "Great product!"
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.rating").value("5"));
    }

    @Test
    void getAllReviewsWorks() throws Exception {
        mvc.perform(get("/api/public/reviews"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getMyReviewsWorks() throws Exception {
        mvc.perform(get("/api/public/reviews/me")
                .with(user("admin").roles("USER"))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Administrator"))
        ;
    }

    @Test
    void getReviewsByShopItemWorks() throws Exception {
        mvc.perform(get("/api/public/reviews/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getAverageRatingByShopItemWorks() throws Exception {
        mvc.perform(get("/api/public/reviews/items/1/Avg"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5.0));
    }

    @Test
    void getReviewAmountByShopItemWorks() throws Exception {
        mvc.perform(get("/api/public/reviews/items/1/Amount"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }

    @Test
    void getMyReviewForItemWorks() throws Exception {
        mvc.perform(get("/api/public/reviews/items/1")
                .with(user("admin").roles("USER"))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].customerName").value("Administrator"))
                .andExpect(jsonPath("$.[0].shopItemId").value(1));
    }

    @Test
    void deleteReviewWorks() throws Exception {
        mvc.perform(delete("/api/public/reviews/1")
                .with(user("johndoe").roles("USER"))
        )
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/public/reviews/me")
                .with(user("Administrator").roles("USER"))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
        ;
    }
}
