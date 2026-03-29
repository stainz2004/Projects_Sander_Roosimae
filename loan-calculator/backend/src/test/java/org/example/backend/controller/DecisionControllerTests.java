package org.example.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DecisionControllerTests {

    @Autowired
    private MockMvc mvc;


    @Test
    void getDecisionWorks() throws Exception {
        mvc.perform(post("/api/loan/decision")
                        .contentType("application/json")
                        .content("""
                        {
                            "personalCode": "50410100555",
                            "loanAmount": 5000,
                            "loanPeriod": 24
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void getDecisionInvalidPersonalCode() throws Exception {
        mvc.perform(post("/api/loan/decision")
                        .contentType("application/json")
                        .content("""
                        {
                            "personalCode": "5041010055A",
                            "loanAmount": 5000,
                            "loanPeriod": 24
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDecisionInvalidLoanAmount() throws Exception {
        mvc.perform(post("/api/loan/decision")
                        .contentType("application/json")
                        .content("""
                        {
                            "personalCode": "50410100555",
                            "loanAmount": -1000,
                            "loanPeriod": 24
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDecisionInvalidLoanPeriod() throws Exception {
        mvc.perform(post("/api/loan/decision")
                        .contentType("application/json")
                        .content("""
                        {
                            "personalCode": "50410100555",
                            "loanAmount": 5000,
                            "loanPeriod": -12
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDecisionInvalidRequestBody() throws Exception {
        mvc.perform(post("/api/loan/decision")
                        .contentType("application/json")
                        .content("""
                        {
                            "personalCode": "50410100555",
                            "loanAmount": 5000
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDecisionValidResponseReturnsHigherLoanThenAsked() throws Exception {
        mvc.perform(post("/api/loan/decision")
                        .contentType("application/json")
                        .content("""
                        {
                            "personalCode": "50410100555",
                            "loanAmount": 5000,
                            "loanPeriod": 24
                        }
                        """))
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.loanAmount").value(greaterThan(5000)));
    }

    @Test
    void getDecisionIncreasesLoanPeriodForLowCreditScore() throws Exception {
        mvc.perform(post("/api/loan/decision")
                        .contentType("application/json")
                        .content("""
                        {
                            "personalCode": "50410100553",
                            "loanAmount": 5000,
                            "loanPeriod": 12
                        }
                        """))
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.loanPeriod").value(greaterThan(12)));
    }
}
