package com.example.veebilehekala.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest extends AbstractIntegrationTest{

    @Autowired
    private MockMvc mvc;

    @Test
    void createOrderWorks() throws Exception {
        mvc.perform(post("/api/order")
                        .with(user("johndoe").roles("USER")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("johndoe"))
                .andExpect(jsonPath("$.price").value("100.0"));
    }

    @Test
    void getOrderByIdWorks() throws Exception {
        mvc.perform(get("/api/order/1")
                        .with(user("admin").roles("USER")))
                .andExpect(jsonPath("$.customerName").value("admin"));
    }

    @Test
    void getOrdersByCustomerWorks() throws Exception {
        mvc.perform(get("/api/order/me")
                        .with(user("admin").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName").value("admin"));
    }
}
