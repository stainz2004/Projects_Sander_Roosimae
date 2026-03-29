package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.constants.LoanConstants;
import org.example.backend.dto.DecisionResponse;
import org.example.backend.exception.InvalidLoanAmount;
import org.example.backend.exception.InvalidLoanPeriod;
import org.example.backend.exception.NoLoanException;
import org.springframework.stereotype.Service;

/**
 * A service class that calculates maximum loan that we would approve of.
 * If no loan is approved based on current information then increases the loan period.
 */
@Service
@RequiredArgsConstructor
public class LoanDecisionCalculator {

    /**
     * Calculates the maximum approved loan amount based on the provided credit modifier, loan amount, and loan period.
     * If the initial credit score is below the minimum threshold, it attempts to find an approved loan by extending the loan period.
     *
     * @param creditModifier The credit modifier used to calculate the credit score.
     * @param loanAmount The amount of the loan being evaluated.
     * @param loanPeriod The period of the loan in months.
     * @return A DecisionResponse object containing the approved loan period and amount.
     * @throws InvalidLoanAmount If the loan amount is outside the defined limits.
     * @throws InvalidLoanPeriod If the loan period is outside the defined limits.
     * @throws NoLoanException If no valid loan is found after extending the period.
     */
    public DecisionResponse calculateMaximumApprovedAmount(int creditModifier, long loanAmount, int loanPeriod) {
        if (loanAmount < LoanConstants.MINIMUM_LOAN_AMOUNT || loanAmount > LoanConstants.MAXIMUM_LOAN_AMOUNT) {
            throw new InvalidLoanAmount("Loan amount must be between 2000€ and 10000€!");
        }
        if (loanPeriod < LoanConstants.MINIMUM_LOAN_PERIOD || loanPeriod > LoanConstants.MAXIMUM_LOAN_PERIOD) {
            throw new InvalidLoanPeriod("Loan period must be between 12 and 60 months!");
        }


        double initialScore = getCreditScore(creditModifier, loanAmount, loanPeriod);
        if (initialScore < LoanConstants.MINIMUM_CREDIT_SCORE) {
            return findApprovedLoanWithExtendedPeriod(creditModifier, loanAmount, loanPeriod);
        }

        long maximumLoan = calculateMaximumLoan(creditModifier, loanPeriod);
        return buildDecisionResponse(loanPeriod, maximumLoan);
    }

    /**
     * Finds an approved loan by extending the loan period until a valid loan is found or the maximum period is reached.
     *
     * @param creditModifier The credit modifier used to calculate the credit score.
     * @param loanAmount The amount of the loan being evaluated.
     * @param loanPeriod The initial period of the loan in months.
     * @return A DecisionResponse object containing the approved loan period and amount.
     * @throws NoLoanException If no valid loan is found after extending the period.
     */
    private DecisionResponse findApprovedLoanWithExtendedPeriod(int creditModifier, long loanAmount, int loanPeriod) {
        for (int period = loanPeriod + 1; period <= LoanConstants.MAXIMUM_LOAN_PERIOD; period++) {
            double score = getCreditScore(creditModifier, loanAmount, period);
            if (score >= LoanConstants.MINIMUM_CREDIT_SCORE) {
                return buildDecisionResponse(period, calculateMaximumLoan(creditModifier, period));
            }
        }
        throw new NoLoanException("No valid loan found!");
    }

    /**
     * Calculates the maximum loan amount based on the credit modifier and loan period, ensuring it does not exceed the defined maximum.
     *
     * @param creditModifier The credit modifier used to calculate the loan amount.
     * @param period The loan period in months.
     * @return The calculated maximum loan amount, capped at the defined maximum.
     */
    private long calculateMaximumLoan(int creditModifier, int period) {
        return Math.min(LoanConstants.MAXIMUM_LOAN_AMOUNT, (long) creditModifier * period);
    }

    /**
     * Calculates the credit score based on the credit modifier, loan amount, and loan period.
     *
     * @param creditModifier The credit modifier used in the calculation.
     * @param loanAmount The amount of the loan being evaluated.
     * @param loanPeriod The period of the loan in months.
     * @return The calculated credit score.
     */
    private double getCreditScore(int creditModifier, long loanAmount, int loanPeriod) {
        return ((double) creditModifier / loanAmount) * loanPeriod;
    }

    /**
     * Builds a DecisionResponse object with the given loan period and loan amount.
     *
     * @param loanPeriod The period of the loan in months.
     * @param loanAmount The amount of the loan being approved.
     * @return A DecisionResponse object containing the loan period and amount.
     */
    private DecisionResponse buildDecisionResponse(int loanPeriod, long loanAmount) {
        DecisionResponse decisionResponse = new DecisionResponse();
        decisionResponse.setLoanPeriod(loanPeriod);
        decisionResponse.setLoanAmount(loanAmount);
        return decisionResponse;
    }
}
