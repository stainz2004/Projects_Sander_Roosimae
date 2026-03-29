package org.example.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.DecisionRequest;
import org.example.backend.dto.DecisionResponse;
import org.example.backend.service.DecisionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loan")
@RequiredArgsConstructor
public class DecisionController {

    private final DecisionService decisionService;


    /**
     * An endpoint that handles loan decision requests.
     * Endpoint either returns a valid result, if any data is wrong then returns an exception containing of the cause.
     *
     * @param decisionRequest Requests body that contains customers ID, preferred loan amount and loan period.
     * @return A ResponseEntity with the decision body containing of the approved loan amount and period.
     */
    @PostMapping("/decision")
    public ResponseEntity<DecisionResponse> requestDecision(@Valid @RequestBody DecisionRequest decisionRequest) {
        DecisionResponse decisionResponse = decisionService.calculateLoanDecision(decisionRequest);
        return ResponseEntity.ok().body(decisionResponse);
    }
}
