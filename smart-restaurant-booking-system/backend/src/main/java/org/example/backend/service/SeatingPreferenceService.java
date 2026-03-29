package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.SeatingPreferenceResponseDto;
import org.example.backend.mapper.SeatingPreferenceMapper;
import org.example.backend.repository.SeatingPreferenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatingPreferenceService {

    private final SeatingPreferenceRepository seatingPreferenceRepository;
    private final SeatingPreferenceMapper seatingPreferenceMapper;

    public List<SeatingPreferenceResponseDto> getSeatingPreferences() {
        return seatingPreferenceMapper.toResponseDto(seatingPreferenceRepository.findAll());
    }
}
