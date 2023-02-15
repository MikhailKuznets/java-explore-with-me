package ru.practicum.mainservice.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.NewEventDto;
import ru.practicum.mainservice.event.dto.UpdateEventUserRequest;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.EventState;
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
    private final EventValidator eventValidator;

    public EventFullDto createEvent(NewEventDto newEventDto, Long userId) {
        LocalDateTime createdTime = LocalDateTime.now().withNano(0);

        // Валидация
        eventValidator.validateNewEventDto(newEventDto);
        User initiator = findUser(userId);
        Long catId = newEventDto.getCategory();
        Category category = findCategory(catId);

        Event newEvent = eventMapper.toEvent(newEventDto);
        newEvent.setCreatedOn(createdTime);
        newEvent.setInitiator(initiator);
        newEvent.setCategory(category);
        return eventMapper.toFullEventDto(eventRepository.save(newEvent));
    }

    public EventFullDto getEventById(Long eventId, Long userId) {
        findUser(userId);
        return eventMapper.toFullEventDto(findEvent(eventId));
    }

    public EventFullDto updateEventByUser(Long eventId, Long userId, UpdateEventUserRequest updateEventUserRequest) {
        findUser(userId);

        String updatedAnnotation = updateEventUserRequest.getAnnotation();
        String updatedDescription = updateEventUserRequest.getDescription();
        Long updatedCategory = updateEventUserRequest.getCategory();
        LocalDateTime updatedEventDate = updateEventUserRequest.getEventDate();

        Event selectedEvent = findEvent(eventId);
        if (!selectedEvent.getState().equals(EventState.PUBLISHED)) {
            throw new NonUpdatedEventException("Only pending or canceled events can be changed");
        }

//        if (updateEventUserRequest.getAnnotation() != null)
//            selectedEvent.setAnnotation();

        return null;
    }


    // Поиск Event / User / Category
    private Event findEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            throw new InvalidIdException("EVENT with eventID = " + eventId + " does not exist");
        });
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new InvalidIdException("USER with userID = " + userId + " does not exist");
        });
    }

    private Category findCategory(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> {
            throw new InvalidIdException("CATEGORY with categoryID = " + catId + " does not exist");
        });
    }


}
