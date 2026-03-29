package org.example.backend.service;

import org.example.backend.constants.LoanConstants;
import org.example.backend.exception.PersonalCodeException;
import org.springframework.stereotype.Service;

/**
 * A service class that extracts the credit modifier from customers personal code.
 * Gets the credit modifier based on the last number of the personal code.
 */
@Service
public class CreditModifierCalculator {


    /**
     * Validates that the personal code is not null, blank and is 11 characters long (to mimic a somewhat real life situation).
     * Checks the last digit of the personal code to see what category it falls under of.
     * lastDigit < 2 -> Debt
     * 2 <= lastDigit < 4 -> Credit score 100
     * 4 <= lastDigit < 7 -> Credit score 300
     * 7 <= lastDigit < 9 -> Credit score 1000
     *
     * @param personalCode Customers personal code.
     * @return The credit modifier based on customers personal code.
     * @throws PersonalCodeException If the personal code is null, blank, not 11 characters long or contains characters.
     */
    public int getCreditModifier(String personalCode) {
        if (personalCode == null || personalCode.isBlank()) {
            throw new PersonalCodeException("Personal code cannot be empty");
        }

        if (personalCode.length() != 11) {
            throw new PersonalCodeException("Personal code must be 11 characters long!");
        }

        int lastDigit = extractLastDigit(personalCode);

        if (lastDigit < 2) return LoanConstants.SEGMENT_DEBT_CREDIT_MODIFIER;
        if (lastDigit < 4) return LoanConstants.SEGMENT_1_CREDIT_MODIFIER;
        if (lastDigit < 7) return LoanConstants.SEGMENT_2_CREDIT_MODIFIER;
        return LoanConstants.SEGMENT_3_CREDIT_MODIFIER;
    }

    /**
     * Extracts the last number of customers personal code.
     *
     * @param personalCode Customers personal code.
     * @return The last digit of the personal code.
     * @throws PersonalCodeException If the last character of the personal code is not a digit.
     */
    private static int extractLastDigit(String personalCode) {
        char lastChar = personalCode.charAt(personalCode.length() - 1);
        if (!Character.isDigit(lastChar)) {
            throw new PersonalCodeException("Personal code must not contain characters!");
        }
        return Character.getNumericValue(lastChar);
    }
}
