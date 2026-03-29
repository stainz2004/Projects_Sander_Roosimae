package org.example.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class SeatingResponseDto {
    private Long id;
    private String name;
    private Long typeId;
    private int maxPeople;
    private List<Long> preferenceIds;
}
