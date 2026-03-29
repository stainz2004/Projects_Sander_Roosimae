package org.example.backend.mapper;

import org.example.backend.dto.SeatingTypeResponseDto;
import org.example.backend.entity.SeatingType;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatingTypeMapper {

    List<SeatingTypeResponseDto> toResponseDto(List<SeatingType> seatingTypes);
}
