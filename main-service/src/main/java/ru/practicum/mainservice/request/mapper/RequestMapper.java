package ru.practicum.mainservice.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.model.ParticipationRequest;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    @Mapping(source = "requester.id", target = "requester")
    @Mapping(source = "event.id", target = "event")
    ParticipationRequestDto toRequestDto(ParticipationRequest participationRequest);

}
