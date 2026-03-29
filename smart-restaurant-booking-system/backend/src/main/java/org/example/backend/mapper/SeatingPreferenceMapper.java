package org.example.backend.mapper;

import org.example.backend.dto.SeatingPreferenceResponseDto;
import org.example.backend.entity.SeatingPreference;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatingPreferenceMapper {

    List<SeatingPreferenceResponseDto> toResponseDto(List<SeatingPreference> seatingPreferences);
}
