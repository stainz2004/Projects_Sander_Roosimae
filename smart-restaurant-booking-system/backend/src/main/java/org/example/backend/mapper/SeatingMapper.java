package org.example.backend.mapper;

import org.example.backend.dto.SeatingFilterResponseDto;
import org.example.backend.dto.SeatingResponseDto;
import org.example.backend.entity.Seating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatingMapper {

    @Mapping(source = "seatingType.id", target = "typeId")
    @Mapping(target = "preferenceIds", expression = "java(seating.getPreferences().stream().map(p -> p.getId()).collect(java.util.stream.Collectors.toList()))")
    SeatingResponseDto toResponseDto(Seating seating);

    List<SeatingResponseDto> toResponseDto(List<Seating> seating);

    List<SeatingFilterResponseDto> toFilterResponseDto(List<Seating> seating);
}
