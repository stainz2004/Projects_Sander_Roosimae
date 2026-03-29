package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.SeatingTypeResponseDto;
import org.example.backend.mapper.SeatingTypeMapper;
import org.example.backend.repository.SeatingTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatingTypeService {

    private final SeatingTypeRepository seatingTypeRepository;
    private final SeatingTypeMapper seatingTypeMapper;

    public List<SeatingTypeResponseDto> getAllSeatingTypes() {
        return seatingTypeMapper.toResponseDto(seatingTypeRepository.findAll());
    }
}
