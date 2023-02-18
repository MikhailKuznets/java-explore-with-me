package ru.practicum.mainservice.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.model.Request;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    ParticipationRequestDto toRequestDto(Request request);

}
