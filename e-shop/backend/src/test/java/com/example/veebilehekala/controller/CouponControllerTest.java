package com.example.veebilehekala.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CouponControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void addCoupon() throws Exception {
        mvc.perform(post("/api/admin/coupons")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
            {
              "code": "TEST10",
              "discount": 0.2
            }
            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true));
    }

    @Test
    void addCoupon_Unauthorized() throws Exception {
        mvc.perform(post("/api/admin/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
            {
              "code": "TEST10",
              "discount": 0.2
            }
            """))
                .andExpect(status().isForbidden());
    }

    @Test
    void getCouponByCode() throws Exception {
        // Now, retrieve the coupon by code
        mvc.perform(get("/api/public/coupons/DISCOUNT10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true));
    }
}
