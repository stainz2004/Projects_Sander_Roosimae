package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.DecisionRequest;
import org.example.backend.dto.DecisionResponse;
import org.example.backend.exception.NoLoanException;
import org.springframework.stereotype.Service;

/**
 * A service class that calculates the maximum loan we would approve based on customers ID, loan amount and period.
 */
@Service
@RequiredArgsConstructor
public class DecisionService {

    private final LoanDecisionCalculator loanDecisionCalculator;
    private final CreditModifierCalculator creditModifierCalculator;


    /**
     * Calculates the maximum approved loan amount based on the provided decision request.
     * Service that talks with the controller and calls the necessary services to calculate the maximum approved loan amount and period.
     *
     * @param decisionRequest The request containing the personal code, loan amount, and loan period.
     * @return A DecisionResponse object containing the approved loan period and amount.
     * @throws NoLoanException If no valid loan is found based on the provided information.
     */
    public DecisionResponse calculateLoanDecision(DecisionRequest decisionRequest) {
        String personalCode = decisionRequest.getPersonalCode();
        long loanAmount = decisionRequest.getLoanAmount();
        int loanPeriod = decisionRequest.getLoanPeriod();

        int creditModifier = creditModifierCalculator.getCreditModifier(personalCode);
        if (creditModifier == 0) {
            throw new NoLoanException("No valid loan found!");
        }

        return loanDecisionCalculator.calculateMaximumApprovedAmount(creditModifier, loanAmount, loanPeriod);
    }

}
