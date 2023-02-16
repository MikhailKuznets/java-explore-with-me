package ru.practicum.mainservice.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.NewEventDto;
import ru.practicum.mainservice.event.dto.UpdateEventAdminRequest;
import ru.practicum.mainservice.event.dto.UpdateEventUserRequest;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.model.*;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.event.validation.EventValidator;
import ru.practicum.mainservice.exception.InvalidIdException;
import ru.practicum.mainservice.exception.NonUpdatedEventException;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;

    public EventFullDto createEvent(NewEventDto newEventDto, Long userId) {
        LocalDateTime createdTime = LocalDateTime.now().withNano(0);

        // Валидация
        EventValidator.validateNewEventDto(newEventDto);
        User initiator = findUser(userId);
        Long catId = newEventDto.getCategory();
        Category category = findCategory(catId);

        Event newEvent = eventMapper.toEvent(newEventDto);

        // Установка значений полей Event
        newEvent.setCreatedOn(createdTime);
        newEvent.setInitiator(initiator);
        newEvent.setCategory(category);
        newEvent.setState(EventState.PENDING);

        if (newEventDto.getPaid() == null) {
            newEvent.setPaid(false);
        }
        if (newEventDto.getParticipantLimit() == null) {
            newEvent.setParticipantLimit(0);
        }
        if (newEventDto.getRequestModeration() == null) {
            newEvent.setRequestModeration(true);
        }

        return eventMapper.toFullEventDto(eventRepository.save(newEvent));
    }

    public EventFullDto getEventById(Long eventId, Long userId) {
        findUser(userId);
        return eventMapper.toFullEventDto(findEvent(eventId));
    }

    public EventFullDto updateEventByUser(Long eventId, Long userId, UpdateEventUserRequest updateEventUserRequest) {
        findUser(userId);

        Event selectedEvent = findEvent(eventId);
        EventState state = selectedEvent.getState();

        if (state.equals(EventState.PUBLISHED)) {
            throw new NonUpdatedEventException("Only pending or canceled events can be changed", LocalDateTime.now());
        }

        UtilityEvent utilityEvent = eventMapper.toUtilityEventClass(updateEventUserRequest);
        Event updatedEvent = updateEventAnnotation(selectedEvent, utilityEvent);
        updatedEvent.setState(EventState.CANCELED);
        return eventMapper.toFullEventDto(eventRepository.save(updatedEvent));
    }

    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event selectedEvent = findEvent(eventId);

        AdminEventState stateAction = updateEventAdminRequest.getStateAction();
        EventState state = selectedEvent.getState();

        switch (stateAction) {
            case PUBLISH_EVENT:
                if (!state.equals(EventState.PENDING)) {
                    throw new NonUpdatedEventException("Cannot publish the event because " +
                            "it's not in the right state: " + state
                            , LocalDateTime.now());
                }
                selectedEvent.setState(EventState.PUBLISHED);
                break;
            case REJECT_EVENT:
                if (selectedEvent.getState().equals(EventState.PUBLISHED)) {
                    throw new NonUpdatedEventException("Cannot reject the event because " +
                            "it's not in the right state: " + state
                            , LocalDateTime.now());
                }
                selectedEvent.setState(EventState.CANCELED);
                break;
        }

        UtilityEvent utilityEvent = eventMapper.toUtilityEventClass(updateEventAdminRequest);
        Event updatedEvent = updateEventAnnotation(selectedEvent, utilityEvent);
        return eventMapper.toFullEventDto(eventRepository.save(updatedEvent));
    }

    // Обновление полей класса Event при PATCH-запросах
    private Event updateEventAnnotation(Event event, UtilityEvent utilityEvent) {
        String annotation = utilityEvent.getAnnotation();
        Long categoryId = utilityEvent.getCategory();
        String description = utilityEvent.getDescription();
        LocalDateTime eventDate = utilityEvent.getEventDate();
        Location location = utilityEvent.getLocation();
        Boolean paid = utilityEvent.getPaid();
        Integer participantLimit = utilityEvent.getParticipantLimit();
        Boolean requestModeration = utilityEvent.getRequestModeration();
        String title = utilityEvent.getTitle();

        if (annotation != null) {
            EventValidator.validateAnnotation(annotation);
            event.setAnnotation(annotation);
        }
        if (categoryId != null) {
            Category category = findCategory(categoryId);
            event.setCategory(category);
        }
        if (description != null) {
            EventValidator.validateDescription(description);
            event.setDescription(description);
        }
        if (eventDate != null) {
            EventValidator.validatePatchEventDate(eventDate);
            event.setEventDate(eventDate);
        }
        if (location != null) {
            event.setLocation(location);
        }
        if (paid != null) {
            event.setPaid(paid);
        }
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }
        if (title != null) {
            EventValidator.validateTitle(title);
            event.setTitle(title);
        }
        return event;
    }

    // Поиск Event / User / Category
    private Event findEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            throw new InvalidIdException("Event", eventId, LocalDateTime.now());
        });
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new InvalidIdException("User", userId, LocalDateTime.now());
        });
    }

    private Category findCategory(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> {
            throw new InvalidIdException("Category", catId, LocalDateTime.now());
        });
    }


}
