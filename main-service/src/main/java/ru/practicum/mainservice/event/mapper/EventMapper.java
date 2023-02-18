package ru.practicum.mainservice.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.mainservice.event.dto.*;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.EventState;
import ru.practicum.mainservice.event.updater.UtilityEvent;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    EventFullDto toFullEventDto(Event event);

    EventShortDto toShortEventDto(Event event);

    UtilityEvent toUtilityEventClass(UpdateEventAdminRequest updateEventAdminRequest);

    UtilityEvent toUtilityEventClass(UpdateEventUserRequest updateEventUserRequest);

    default Event toEvent(NewEventDto newEventDto) {
        if (newEventDto == null) {
            return null;
        }
        Boolean paid = newEventDto.getPaid();
        Boolean requestModeration = newEventDto.getRequestModeration();
        Integer participantLimit = newEventDto.getParticipantLimit();

        if (paid == null) {
            paid = false;
        }
        if (requestModeration == null) {
            requestModeration = true;
        }

        if (participantLimit == null) {
            participantLimit = 0;
        }

        Event newEvent = Event.builder()
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(newEventDto.getLocation())
                .createdOn(LocalDateTime.now())
                .confirmedRequests(0)
                .state(EventState.PENDING)
                .title(newEventDto.getTitle())
                .paid(paid)
                .requestModeration(requestModeration)
                .participantLimit(participantLimit)
                .build();
        return newEvent;
    }
}
