package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.SeatingFilterDto;
import org.example.backend.dto.SeatingFilterResponseDto;
import org.example.backend.dto.SeatingResponseDto;
import org.example.backend.service.SeatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/seatings")
@RequiredArgsConstructor
public class SeatingController {

    private final SeatingService seatingService;

    @GetMapping
    public ResponseEntity<List<SeatingResponseDto>> getAllSeating() {
        return ResponseEntity.ok(seatingService.getAllSeating());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<SeatingFilterResponseDto>> getAllSeatingFilter(SeatingFilterDto filter) {
        return ResponseEntity.ok(seatingService.getFilteredSeating(filter));
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<SeatingFilterResponseDto>> getMostMatchingSeating(SeatingFilterDto filter) {
        return ResponseEntity.ok(seatingService.getMostMatchingSeating(filter));
    }

    @GetMapping("/booked")
    public ResponseEntity<List<Long>> getBookedSeatings(@RequestParam LocalDateTime dateAndTime) {
        return ResponseEntity.ok(seatingService.getBookedSeatings(dateAndTime));
    }

}
