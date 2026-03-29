package org.example.backend.dto;

import lombok.Data;

@Data
public class DecisionResponse {
    private Long loanAmount;
    private Integer loanPeriod;
}
