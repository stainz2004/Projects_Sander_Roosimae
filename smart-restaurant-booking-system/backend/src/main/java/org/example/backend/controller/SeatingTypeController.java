package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.SeatingTypeResponseDto;
import org.example.backend.service.SeatingTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seating-types")
public class SeatingTypeController {

    private final SeatingTypeService seatingTypeService;

    @GetMapping
    public ResponseEntity<List<SeatingTypeResponseDto>> getSeatingTypes() {
        return ResponseEntity.ok(seatingTypeService.getAllSeatingTypes());
    }
}
