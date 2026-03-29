package org.example.backend.service;

import org.example.backend.dto.DecisionRequest;
import org.example.backend.dto.DecisionResponse;
import org.example.backend.exception.PersonalCodeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DecisionServiceTests {

    @Mock
    private CreditModifierCalculator creditModifierCalculator;

    @Mock
    private LoanDecisionCalculator loanDecisionCalculator;

    @InjectMocks
    private DecisionService decisionService;

    @Test
    void testCalculateLoanDecision_validInput_returnsApproved() {
        DecisionRequest decisionRequest = new DecisionRequest();
        decisionRequest.setLoanAmount(2400L);
        decisionRequest.setLoanPeriod(24);
        decisionRequest.setPersonalCode("12345678902");

        DecisionResponse decisionResponse = new DecisionResponse();
        decisionResponse.setLoanAmount(2400L);
        decisionResponse.setLoanPeriod(24);

        when(creditModifierCalculator.getCreditModifier("12345678902")).thenReturn(100);
        when(loanDecisionCalculator.calculateMaximumApprovedAmount(100, 2400L, 24)).thenReturn(decisionResponse);

        var response = decisionService.calculateLoanDecision(decisionRequest);

        assert(response.getLoanAmount() == 2400);
        assert(response.getLoanPeriod() == 24);
    }

    @Test
    void testCalculateLoanDecision_noValidLoan_throwsException() {
        DecisionRequest decisionRequest = new DecisionRequest();
        decisionRequest.setLoanAmount(2400L);
        decisionRequest.setLoanPeriod(24);
        decisionRequest.setPersonalCode("12345678901");

        when(creditModifierCalculator.getCreditModifier("12345678901")).thenReturn(0);

        try {
            decisionService.calculateLoanDecision(decisionRequest);
        } catch (Exception e) {
            assert(e.getMessage().equals("No valid loan found!"));
        }
    }

    @Test
    void testCalculateLoanDecision_invalidLoanAmount_throwsException() {
        DecisionRequest decisionRequest = new DecisionRequest();
        decisionRequest.setLoanAmount(1500L);
        decisionRequest.setLoanPeriod(24);
        decisionRequest.setPersonalCode("12345678902");

        when(creditModifierCalculator.getCreditModifier("12345678902")).thenReturn(100);

        try {
            decisionService.calculateLoanDecision(decisionRequest);
        } catch (Exception e) {
            assert(e.getMessage().equals("Loan amount must be between 2000€ and 10000€!"));
        }
    }

    @Test
    void testCalculateLoanDecision_invalidLoanPeriod_throwsException() {
        DecisionRequest decisionRequest = new DecisionRequest();
        decisionRequest.setLoanAmount(2400L);
        decisionRequest.setLoanPeriod(6);
        decisionRequest.setPersonalCode("12345678902");

        when(creditModifierCalculator.getCreditModifier("12345678902")).thenReturn(100);

        try {
            decisionService.calculateLoanDecision(decisionRequest);
        } catch (Exception e) {
            assert (e.getMessage().equals("Loan period must be between 12 and 60 months!"));
        }
    }

    @Test
    void testCalculateLoanDecision_invalidPersonalCode_throwsException() {
        DecisionRequest decisionRequest = new DecisionRequest();
        decisionRequest.setLoanAmount(2400L);
        decisionRequest.setLoanPeriod(24);
        decisionRequest.setPersonalCode("1234567890");

        when(creditModifierCalculator.getCreditModifier("1234567890")).thenThrow(new PersonalCodeException("Personal code must be 11 characters long!"));

        try {
            decisionService.calculateLoanDecision(decisionRequest);
        } catch (Exception e) {
            assert (e.getMessage().equals("Personal code must be 11 characters long!"));
        }
    }

    @Test
    void testCalculateLoanDecision_increasingLoanPeriodFindsValidLoan() {
        DecisionRequest decisionRequest = new DecisionRequest();
        decisionRequest.setLoanAmount(6600L);
        decisionRequest.setLoanPeriod(12);
        decisionRequest.setPersonalCode("12345678904");

        DecisionResponse decisionResponse = new DecisionResponse();
        decisionResponse.setLoanAmount(6600L);
        decisionResponse.setLoanPeriod(22);

        when(creditModifierCalculator.getCreditModifier("12345678904")).thenReturn(300);
        when(loanDecisionCalculator.calculateMaximumApprovedAmount(300, 6600L, 12)).thenReturn(decisionResponse);

        var response = decisionService.calculateLoanDecision(decisionRequest);

        assert(response.getLoanAmount() == 6600);
        assert(response.getLoanPeriod() == 22);
    }
}
