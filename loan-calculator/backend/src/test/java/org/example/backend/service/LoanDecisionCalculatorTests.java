package org.example.backend.service;

import org.example.backend.exception.InvalidLoanAmount;
import org.example.backend.exception.InvalidLoanPeriod;
import org.example.backend.exception.NoLoanException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoanDecisionCalculatorTests {

    @InjectMocks
    private LoanDecisionCalculator loanDecisionCalculator;

    @Test
    void testCalculateMaximumApprovedAmount_validInput_returnsApprovedLoan() {
        int creditModifier = 100;
        long loanAmount = 2400;
        int loanPeriod = 24;

        var response = loanDecisionCalculator.calculateMaximumApprovedAmount(creditModifier, loanAmount, loanPeriod);

        assert(response.getLoanAmount() == 2400);
        assert(response.getLoanPeriod() == 24);
    }

    @Test
    void testCalculateMaximumApprovedAmount_perfectCreditScore_returnsMaximumLoan() {
        int creditModifier = 1000;
        long loanAmount = 2000;
        int loanPeriod = 12;

        var response = loanDecisionCalculator.calculateMaximumApprovedAmount(creditModifier, loanAmount, loanPeriod);

        assert(response.getLoanAmount() == 10000);
        assert(response.getLoanPeriod() == 12);
    }

    @Test
    void testCalculateMaximumApprovedAmount_lowCreditScore_returnsApprovedLoanWithExtendedPeriod() {
        int creditModifier = 100;
        long loanAmount = 5000;
        int loanPeriod = 12;

        var response = loanDecisionCalculator.calculateMaximumApprovedAmount(creditModifier, loanAmount, loanPeriod);

        assert(response.getLoanAmount() == 5000);
        assert(response.getLoanPeriod() == 50);
    }

    @Test
    void testCalculateMaximumApprovedAmount_invalidLoanAmountLowerBound_throwsException() {
        int creditModifier = 100;
        long loanAmount = 1999;
        int loanPeriod = 12;

        try {
            loanDecisionCalculator.calculateMaximumApprovedAmount(creditModifier, loanAmount, loanPeriod);
        } catch (Exception e) {
            assert(e instanceof InvalidLoanAmount);
            assert(e.getMessage().equals("Loan amount must be between 2000€ and 10000€!"));
        }
    }

    @Test
    void testCalculateMaximumApprovedAmount_invalidLoanAmountUpperBound_throwsException() {
        int creditModifier = 100;
        long loanAmount = 10001;
        int loanPeriod = 12;

        try {
            loanDecisionCalculator.calculateMaximumApprovedAmount(creditModifier, loanAmount, loanPeriod);
        } catch (Exception e) {
            assert(e instanceof InvalidLoanAmount);
            assert(e.getMessage().equals("Loan amount must be between 2000€ and 10000€!"));
        }
    }

    @Test
    void testCalculateMaximumApprovedAmount_invalidLoanPeriodLowerBound_throwsException() {
        int creditModifier = 100;
        long loanAmount = 5000;
        int loanPeriod = 11;

        try {
            loanDecisionCalculator.calculateMaximumApprovedAmount(creditModifier, loanAmount, loanPeriod);
        } catch (Exception e) {
            assert(e instanceof InvalidLoanPeriod);
            assert(e.getMessage().equals("Loan period must be between 12 and 60 months!"));
        }
    }

    @Test
    void testCalculateMaximumApprovedAmount_invalidLoanPeriodUpperBound_throwsException() {
        int creditModifier = 100;
        long loanAmount = 5000;
        int loanPeriod = 61;

        try {
            loanDecisionCalculator.calculateMaximumApprovedAmount(creditModifier, loanAmount, loanPeriod);
        } catch (Exception e) {
            assert (e instanceof InvalidLoanPeriod);
            assert (e.getMessage().equals("Loan period must be between 12 and 60 months!"));
        }
    }

    @Test
    void testCalculateMaximumApprovedAmount_noValidLoanFound_throwsException() {
        int creditModifier = 100;
        long loanAmount = 10000;
        int loanPeriod = 12;

        try {
            loanDecisionCalculator.calculateMaximumApprovedAmount(creditModifier, loanAmount, loanPeriod);
        } catch (Exception e) {
            assert(e instanceof NoLoanException);
            assert(e.getMessage().equals("No valid loan found!"));
        }
    }

    @Test
    void testCalculateMaximumApprovedAmount_creditModifierExactlyAtThreshold_returnsApprovedLoan() {
        int creditModifier = 300;
        long loanAmount = 3600;
        int loanPeriod = 12;

        var response = loanDecisionCalculator.calculateMaximumApprovedAmount(creditModifier, loanAmount, loanPeriod);

        assert(response.getLoanAmount() == 3600);
        assert(response.getLoanPeriod() == 12);
    }
}
