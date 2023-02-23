package ru.practicum.mainservice.event.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.comment.mapper.CommentMapper;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.comment.repository.CommentRepository;
import ru.practicum.mainservice.controllers.admincontrollers.parameters.EventAdminRequestParameters;
import ru.practicum.mainservice.controllers.publiccontrollers.parameters.EventPublicRequestParameters;
import ru.practicum.mainservice.controllers.publiccontrollers.parameters.EventRequestSort;
import ru.practicum.mainservice.event.dto.*;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.model.*;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.event.updater.EventUpdater;
import ru.practicum.mainservice.event.updater.UtilityEvent;
import ru.practicum.mainservice.event.validation.EventValidator;
import ru.practicum.mainservice.exception.InvalidIdException;
import ru.practicum.mainservice.exception.event.NonUpdatedEventException;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;
import ru.practicum.statclient.StatClient;
import ru.practicum.statdto.RequestHitDto;
import ru.practicum.statdto.ViewsStatsRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final EventMapper eventMapper;
    private final CommentMapper commentMapper;
    private final StatClient statClient;


    public EventFullDto createEvent(NewEventDto newEventDto, Long userId) {
        // Валидация
        EventValidator.validateNewEventDto(newEventDto);
        User initiator = findUser(userId);
        Long catId = newEventDto.getCategory();
        Category category = findCategory(catId);

        Event newEvent = eventMapper.toEvent(newEventDto);

        // Установка значений полей Event
        newEvent.setInitiator(initiator);
        newEvent.setCategory(category);
        return eventMapper.toFullEventDto(eventRepository.save(newEvent));
    }

    public EventFullDto getPrivateEventById(Long eventId, Long userId) {
        findUser(userId);
        EventFullDto eventFullDto = eventMapper.toFullEventDto(findEvent(eventId));
        setViewsToEventFullDto(eventFullDto);
        setComments(eventFullDto);
        return eventFullDto;
    }

    public EventFullDto getPublicEventById(Long eventId, HttpServletRequest request) {
        Event event = findEvent(eventId);
        addHit(request);
        EventFullDto eventFullDto = eventMapper.toFullEventDto(event);
        setViewsToEventFullDto(eventFullDto);
        setComments(eventFullDto);
        return eventFullDto;
    }

    public Collection<EventShortDto> getPublicEventsWithParameters(
            EventPublicRequestParameters parameters, EventRequestSort sort,
            Integer from, Integer size, HttpServletRequest request) {
        addHit(request);
        parameters.checkTime();

        PageRequest pageRequest = PageRequest.of(from, size);

        BooleanBuilder predicate = getPublicPredicate(parameters);
        Page<Event> events = eventRepository.findAll(predicate, pageRequest);
        List<EventShortDto> eventDtos = events.stream()
                .map(eventMapper::toShortEventDto)
                .map(this::setViewsToShortDto)
                .collect(Collectors.toList());
        switch (sort) {
            case EVENT_DATE:
                eventDtos.sort(Comparator.comparing(EventShortDto::getEventDate));
                break;
            case VIEWS:
                eventDtos.sort(Comparator.comparing(EventShortDto::getViews));
                break;
        }
        return eventDtos;
    }

    public Collection<EventFullDto> getAdminEventsWithParameters(EventAdminRequestParameters parameters,
                                                                 Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from, size);

        BooleanBuilder predicate = getAdminPredicate(parameters);
        Page<Event> events = eventRepository.findAll(predicate, pageRequest);
        return events.stream()
                .map(eventMapper::toFullEventDto)
                .map(this::setViewsToEventFullDto)
                .map(this::setComments)
                .collect(Collectors.toList());
    }

    public Collection<EventShortDto> getAllUserEvents(Long unitiatorId, Integer from, Integer size) {
        findUser(unitiatorId);
        PageRequest pageRequest = PageRequest.of(from, size);
        return eventRepository.findAllByInitiator_Id(unitiatorId, pageRequest).stream()
                .map(eventMapper::toShortEventDto)
                .map(this::setViewsToShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDto updateEventByUser(Long eventId, Long userId, UpdateEventUserRequest updateEventUserRequest) {
        findUser(userId);

        Event selectedEvent = findEvent(eventId);
        EventState state = selectedEvent.getState();

        if (state.equals(EventState.PUBLISHED)) {
            throw new NonUpdatedEventException("Only pending or canceled events can be changed", LocalDateTime.now());
        }
        UtilityEvent utilityEvent = eventMapper.toUtilityEventClass(updateEventUserRequest);
        Event updatedEvent = EventUpdater.updateEventAnnotation(selectedEvent, utilityEvent);
        UpdateEventUserState stateAction = updateEventUserRequest.getStateAction();
        switch (stateAction) {
            case CANCEL_REVIEW:
                updatedEvent.setState(EventState.CANCELED);
                break;
            case SEND_TO_REVIEW:
                updatedEvent.setState(EventState.PENDING);
                break;
        }
        updatedEvent = updateEventCategory(updatedEvent, utilityEvent.getCategory());
        EventFullDto eventFullDto = eventMapper.toFullEventDto(eventRepository.save(updatedEvent));
        setViewsToEventFullDto(eventFullDto);
        setComments(eventFullDto);
        return eventFullDto;
    }

    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event selectedEvent = findEvent(eventId);

        AdminEventState stateAction = updateEventAdminRequest.getStateAction();
        EventState state = selectedEvent.getState();

        if (stateAction != null) {
            switch (stateAction) {
                case PUBLISH_EVENT:
                    if (!state.equals(EventState.PENDING)) {
                        throw new NonUpdatedEventException("Cannot publish the event because " +
                                "it's not in the right state: " + state, LocalDateTime.now());
                    }
                    selectedEvent.setState(EventState.PUBLISHED);
                    break;
                case REJECT_EVENT:
                    if (selectedEvent.getState().equals(EventState.PUBLISHED)) {
                        throw new NonUpdatedEventException("Cannot reject the event because " +
                                "it's not in the right state: " + state, LocalDateTime.now());
                    }
                    selectedEvent.setState(EventState.CANCELED);
                    break;
            }
        }

        UtilityEvent utilityEvent = eventMapper.toUtilityEventClass(updateEventAdminRequest);
        Event updatedEvent = EventUpdater.updateEventAnnotation(selectedEvent, utilityEvent);
        updatedEvent = updateEventCategory(updatedEvent, utilityEvent.getCategory());
        updatedEvent.setPublishedOn(LocalDateTime.now());
        EventFullDto eventFullDto = eventMapper.toFullEventDto(eventRepository.save(updatedEvent));
        setViewsToEventFullDto(eventFullDto);
        setComments(eventFullDto);
        return eventFullDto;
    }


    //              PRIVATE METHODS

    private Event updateEventCategory(Event event, Long newCatId) {
        if (newCatId != null) {
            Category newCategory = findCategory(newCatId);
            event.setCategory(newCategory);
        }
        return event;
    }

    private EventFullDto setComments(EventFullDto eventFullDto) {
        Long eventId = eventFullDto.getId();
        Collection<Comment> comments = commentRepository.findAllByEvent_Id(eventId);
        eventFullDto.setComments(comments.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList()));
        return eventFullDto;
    }

    // Подготовка предикатов
    private BooleanBuilder getPublicPredicate(EventPublicRequestParameters parameters) {
        BooleanBuilder predicate = new BooleanBuilder();

        String text = parameters.getText();
        List<Long> catIds = parameters.getCatIds();
        Boolean paid = parameters.getPaid();
        LocalDateTime rangeStart = parameters.getRangeStart();
        LocalDateTime rangeEnd = parameters.getRangeEnd();
        Boolean onlyAvailable = parameters.getOnlyAvailable();

        if (text != null) {
            predicate.and(QEvent.event.annotation.likeIgnoreCase(text)
                    .or(QEvent.event.description.likeIgnoreCase(text)));
        }
        if (!catIds.isEmpty()) {
            predicate.and(QEvent.event.category.id.in(catIds));
        }
        if (paid != null) {
            predicate.and(QEvent.event.paid.eq(paid));
        }

        predicate.and(QEvent.event.eventDate.after(rangeStart));
        predicate.and(QEvent.event.eventDate.before(rangeEnd));

        if (onlyAvailable) {
            predicate.and(QEvent.event.participantLimit.eq(0)
                    .or(QEvent.event.participantLimit.lt(QEvent.event.confirmedRequests)));
        }
        return predicate;
    }

    private BooleanBuilder getAdminPredicate(EventAdminRequestParameters parameters) {
        BooleanBuilder predicate = new BooleanBuilder();

        List<Long> userIds = parameters.getUserIds();
        List<EventState> states = parameters.getStates();
        List<Long> catIds = parameters.getCatIds();
        LocalDateTime rangeStart = parameters.getRangeStart();
        LocalDateTime rangeEnd = parameters.getRangeEnd();

        if (!userIds.isEmpty()) {
            predicate.and(QEvent.event.initiator.id.in(userIds));
        }
        if (!states.isEmpty()) {
            predicate.and(QEvent.event.state.in(states));
        }
        if (!catIds.isEmpty()) {
            predicate.and(QEvent.event.category.id.in(catIds));
        }
        if (rangeStart != null) {
            predicate.and(QEvent.event.category.id.in(catIds));
        }
        if (rangeEnd != null) {
            predicate.and(QEvent.event.eventDate.before(rangeEnd));
        }
        return predicate;
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

    // Статистики
    private void addHit(HttpServletRequest request) {
        String app = "ewm-main";
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();

        RequestHitDto requestHitDto = new RequestHitDto(app,
                uri,
                ip,
                LocalDateTime.now());
        statClient.saveHit(requestHitDto);
    }

    // Установка количества просмотров в EventFullDto
    private EventFullDto setViewsToEventFullDto(EventFullDto eventFullDto) {
        Long eventId = eventFullDto.getId();
        Integer views = getViews(eventId);
        eventFullDto.setViews(views);
        return eventFullDto;
    }

    // Установка количества просмотров в EventShortDto
    private EventShortDto setViewsToShortDto(EventShortDto eventShortDto) {
        Long eventId = eventShortDto.getId();
        Integer views = getViews(eventId);
        eventShortDto.setViews(views);
        return eventShortDto;
    }

    // Получение количества просмотров определенного Эвента
    private Integer getViews(Long eventId) {
        Event event = findEvent(eventId);
        LocalDateTime start = event.getCreatedOn();
        LocalDateTime end = LocalDateTime.now();
        String[] uris = {"/events/" + eventId.toString()};
        ObjectMapper objectMapper = new ObjectMapper();
        Integer views = 0;

        List<ViewsStatsRequest> stat = objectMapper
                .convertValue(statClient.getStat(start, end, uris, false).getBody(), new TypeReference<>() {
                });

        if (!stat.isEmpty()) {
            views = stat.get(0).getHits();
        }

        return views;
    }

}
