package ru.practicum.mainservice.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.EventState;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.InvalidIdException;
import ru.practicum.mainservice.exception.NonCanceledRequestException;
import ru.practicum.mainservice.exception.ParticipantLimitException;
import ru.practicum.mainservice.exception.UnCreatedRequestException;
import ru.practicum.mainservice.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.mapper.RequestMapper;
import ru.practicum.mainservice.request.model.ParticipationRequest;
import ru.practicum.mainservice.request.model.RequestStatus;
import ru.practicum.mainservice.request.repository.RequestRepository;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public
class RequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    public Collection<ParticipationRequestDto> getUserRequests(Long requesterId) {
        findUser(requesterId);
        Collection<ParticipationRequest> requests = requestRepository.findAllByRequester_Id(requesterId);
        return requests.stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    public Collection<ParticipationRequestDto> getRequestsForInitiator(Long eventId, Long userId) {
        findUser(userId);
        findEvent(eventId);
        Collection<ParticipationRequest> requests = requestRepository.findAllByRequester_IdAndEvent_Id(userId, eventId);
        return requests.stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
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

        ParticipationRequest participationRequest = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .status(RequestStatus.PENDING)
                .build();

        // Проверка пре-модерации и изменение confirmedRequests
        if (!event.getRequestModeration()) {
            participationRequest.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(confirmedRequests++);
            eventRepository.save(event);
        }

        return requestMapper.toRequestDto(requestRepository.save(participationRequest));
    }

    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        User user = findUser(userId);
        ParticipationRequest request = findRequest(requestId);
        if (!request.getRequester().equals(user)) {
            throw new NonCanceledRequestException("It is not possible to cancel another user's request",
                    LocalDateTime.now());
        }
        if (request.getStatus().equals(RequestStatus.CANCELED)) {
            throw new NonCanceledRequestException("The request has already been canceled",
                    LocalDateTime.now());
        }
        request.setStatus(RequestStatus.CANCELED);
        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    public EventRequestStatusUpdateResult updateRequestStatus(Long eventId, Long userId,
                                                              EventRequestStatusUpdateRequest request) {
        Event event = findEvent(eventId);
        User user = findUser(userId);
        List<Long> requestIds = request.getRequestIds();
        requestIds.forEach(this::findRequest);

        Integer confirmedRequests = event.getConfirmedRequests();
        Integer participantLimit = event.getParticipantLimit();
        if (participantLimit != 0 && (participantLimit <= confirmedRequests)) {
            throw new ParticipantLimitException("The participant limit has been reached", LocalDateTime.now());
        }

        Collection<ParticipationRequest> requests = requestRepository.findByIdIn(requestIds);
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

    private ParticipationRequest findRequest(Long requestId) {
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
