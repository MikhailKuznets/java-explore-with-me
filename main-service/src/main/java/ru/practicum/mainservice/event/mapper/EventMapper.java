package ru.practicum.mainservice.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.mainservice.event.dto.*;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.updater.UtilityEvent;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "category.id", source = "category")
    Event toEvent(NewEventDto newEventDto);

    EventFullDto toFullEventDto(Event event);

    EventShortDto toShortEventDto(Event event);

    UtilityEvent toUtilityEventClass(UpdateEventAdminRequest updateEventAdminRequest);

    UtilityEvent toUtilityEventClass(UpdateEventUserRequest updateEventUserRequest);
}
