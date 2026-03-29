package org.example.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SeatingFilterDto {
    private LocalDateTime dateAndTime;
    private Integer numberOfPeople;
    private Long seatingTypeId;
    private Long selectedPreference;
}