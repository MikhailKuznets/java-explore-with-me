package ru.practicum.mainservice.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.EventState;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.InvalidIdException;
import ru.practicum.mainservice.exception.UnCreatedRequestException;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.mapper.RequestMapper;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.model.RequestStatus;
import ru.practicum.mainservice.request.repository.RequestRepository;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public
class RequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    public Collection<ParticipationRequestDto> getUserRequests(Long requesterId) {
        User requester = findUser(requesterId);

        return null;
    }

    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User requester = findUser(userId);
        Event event = findEvent(eventId);

        // Возможные ошибки
        checkRequestExistence(userId, eventId);
        Integer confirmedRequests = event.getConfirmedRequests();
        Integer participantLimit = event.getParticipantLimit();
        if (participantLimit != 0 && (participantLimit <= confirmedRequests)) {
            throw new UnCreatedRequestException("The limit of participation requests has been reached",
                    LocalDateTime.now());
        }
        if (event.getInitiator().equals(requester)) {
            throw new UnCreatedRequestException("The initiator of the event cannot add a request to participate " +
                    "in his event", LocalDateTime.now());
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new UnCreatedRequestException("You cannot participate in an unpublished event", LocalDateTime.now());
        }

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .status(RequestStatus.PENDING)
                .build();

        // Проверка пре-модерации и изменение confirmedRequests
        if (!event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(confirmedRequests++);
            eventRepository.save(event);
        }

        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        User requester = findUser(userId);

        return null;
    }


    // Поиск Event / User / Request
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

    private Request findRequest(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> {
            throw new InvalidIdException("Request", requestId, LocalDateTime.now());
        });
    }

    private void checkRequestExistence(Long userId, Long eventId) {
        if (requestRepository.findByRequester_IdAndEvent_Id(userId, eventId).isPresent()) {
            throw new UnCreatedRequestException("The limit of participation requests has been reached",
                    LocalDateTime.now());
        }
    }

}
