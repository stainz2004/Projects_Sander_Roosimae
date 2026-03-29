package com.example.veebilehekala.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SignInControllerTest extends AbstractIntegrationTest{

    @Autowired
    private MockMvc mvc;

    @Test
    void addCustomerWorks() throws Exception {
        mvc.perform(post("/api/public/signin")
                .contentType("application/json")
                .content("""
                        {
                            "username": "testuser2",
                            "name": "Test User2",
                            "password": "TestPassword123",
                            "email": "test@gmail.com"
                        }
                            """)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void customerLoginWorks() throws Exception {
        // First, create a user to log in
        mvc.perform(post("/api/public/login")
                        .contentType("application/json")
                        .content("""

                                {
                            "username": "johndoe",
                            "password": "s"
                            }
                            """)
                )
                .andExpect(status().isOk());
        }

    @Test
    void getCurrentUserWorks() throws Exception {
        mvc.perform(get("/api/me")
                        .with(user("johndoe").roles("USER"))
                )
                .andExpect(status().isOk());
    }

}
