package ru.practicum.mainservice.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.NewEventDto;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.InvalidIdException;
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
        User initiator = findUser(userId);
        Long catId = newEventDto.getCategory();
        Category category = findCategory(catId);

        Event newEvent = eventMapper.toEvent(newEventDto);
        newEvent.setCreatedOn(createdTime);
        newEvent.setInitiator(initiator);
        newEvent.setCategory(category);
        return eventMapper.toFullEventDto(eventRepository.save(newEvent));
    }


    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new InvalidIdException("User with ID = " + userId + " does not exist");
        });
    }

    private Category findCategory(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> {
            throw new InvalidIdException("Category with ID = " + catId + " does not exist");
        });
    }

}
