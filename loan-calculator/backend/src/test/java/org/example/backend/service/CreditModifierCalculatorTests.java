package org.example.backend.service;

import org.example.backend.exception.PersonalCodeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CreditModifierCalculatorTests {

    @InjectMocks
    private CreditModifierCalculator creditModifierCalculator;

    @Test
    void testGetCreditModifier_validPersonalCode_returnsCorrectModifierDebt() {
        String personalCode = "12345678900";
        String personalCodeEndsWith1 = "12345678901";

        int creditModifierEndsWith0 = creditModifierCalculator.getCreditModifier(personalCode);
        int creditModifierEndsWith1 = creditModifierCalculator.getCreditModifier(personalCodeEndsWith1);

        assert(creditModifierEndsWith0 == 0);
        assert(creditModifierEndsWith1 == 0);
    }

    @Test
    void testGetCreditModifier_validPersonalCodeReturnsCorrectModifierCreditScore100() {
        String personalCodeEndsWith2 = "12345678902";
        String personalCodeEndsWith3 = "12345678903";

        int creditModifier2 = creditModifierCalculator.getCreditModifier(personalCodeEndsWith2);
        int creditModifier3 = creditModifierCalculator.getCreditModifier(personalCodeEndsWith3);

        assert(creditModifier2 == 100);
        assert(creditModifier3 == 100);
    }

    @Test
    void testGetCreditModifier_validPersonalCodeReturnsCorrectModifierCreditScore300() {
        String personalCodeEndsWith4 = "12345678904";
        String personalCodeEndsWith5 = "12345678905";
        String personalCodeEndsWith6 = "12345678906";

        int creditModifier4 = creditModifierCalculator.getCreditModifier(personalCodeEndsWith4);
        int creditModifier5 = creditModifierCalculator.getCreditModifier(personalCodeEndsWith5);
        int creditModifier6 = creditModifierCalculator.getCreditModifier(personalCodeEndsWith6);

        assert(creditModifier4 == 300);
        assert(creditModifier5 == 300);
        assert(creditModifier6 == 300);
    }

    @Test
    void testGetCreditModifier_validPersonalCodeReturnsCorrectModifierCreditScore1000() {
        String personalCodeEndsWith7 = "12345678907";
        String personalCodeEndsWith8 = "12345678908";
        String personalCodeEndsWith9 = "12345678909";

        int creditModifier7 = creditModifierCalculator.getCreditModifier(personalCodeEndsWith7);
        int creditModifier8 = creditModifierCalculator.getCreditModifier(personalCodeEndsWith8);
        int creditModifier9 = creditModifierCalculator.getCreditModifier(personalCodeEndsWith9);

        assert(creditModifier7 == 1000);
        assert(creditModifier8 == 1000);
        assert(creditModifier9 == 1000);
    }

    @Test
    void testGetCreditModifier_invalidPersonalCodeNotLongEnough_throwsException() {
        String personalCode = "1234567890";

        assertThrows(PersonalCodeException.class, () -> creditModifierCalculator.getCreditModifier(personalCode));
    }

    @Test
    void testGetCreditModifier_invalidPersonalCodeContainsCharacters_throwsException() {
        String personalCode = "1234567890A";

        assertThrows(PersonalCodeException.class, () -> creditModifierCalculator.getCreditModifier(personalCode));
    }

    @Test
    void testGetCreditModifier_invalidPersonalCodeIsBlank_throwsException() {
        String personalCode = "   ";

        assertThrows(PersonalCodeException.class, () -> creditModifierCalculator.getCreditModifier(personalCode));
    }

    @Test
    void testGetCreditModifier_invalidPersonalCodeIsNull_throwsException() {
        String personalCode = null;

        assertThrows(PersonalCodeException.class, () -> creditModifierCalculator.getCreditModifier(personalCode));
    }
}
