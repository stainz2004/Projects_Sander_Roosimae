package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.SeatingPreferenceResponseDto;
import org.example.backend.service.SeatingPreferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/seatings/preferences")
@RequiredArgsConstructor
public class SeatingPreferenceController {

    private final SeatingPreferenceService seatingPreferenceService;

    @GetMapping
    public ResponseEntity<List<SeatingPreferenceResponseDto>> getSeatingPreferences() {
        return ResponseEntity.ok(seatingPreferenceService.getSeatingPreferences());
    }
}
