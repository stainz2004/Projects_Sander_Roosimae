package org.example.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DecisionRequest {

    // Simple personal code validation to mock real life example.
    @NotBlank
    @Size(min =11, max =11, message = "Personal code must be 11 characters long!")
    private String personalCode;
    @NotNull
    @Min(12)
    @Max(60)
    private Integer loanPeriod;
    @NotNull
    @Min(2000)
    @Max(10000)
    private Long loanAmount;
}
