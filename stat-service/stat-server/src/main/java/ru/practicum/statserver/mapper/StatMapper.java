package ru.practicum.statserver.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.statdto.RequestHitDto;
import ru.practicum.statdto.ViewsStatsRequest;
import ru.practicum.statserver.model.Hit;


@Mapper(componentModel = "spring")
public interface StatMapper {
    StatMapper INSTANCE = Mappers.getMapper(StatMapper.class);

    Hit toHit(RequestHitDto requestHitDto);

    ViewsStatsRequest toViewStatDto(ViewStat viewStat);
}
