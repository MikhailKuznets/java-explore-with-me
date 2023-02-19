package ru.practicum.mainservice.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.EventState;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.*;
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
import java.util.Collections;
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
        Boolean isAvailable = (participantLimit - confirmedRequests) > 0;

        if (participantLimit != 0 && !isAvailable) {
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
            event.setConfirmedRequests(++confirmedRequests);
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

    public EventRequestStatusUpdateResult updateRequestStatus(Long eventId, Long initiatorId,
                                                              EventRequestStatusUpdateRequest updateRequest) {
        Event event = findEvent(eventId);
        findUser(initiatorId);
        List<Long> requestIds = updateRequest.getRequestIds();
        RequestStatus selectedStatus = updateRequest.getStatus();

        int approvedRequests = event.getConfirmedRequests();
        int participantLimit = event.getParticipantLimit();
        int availableParticipants = participantLimit - approvedRequests;
        int potentialParticipants = requestIds.size();

        List<ParticipationRequestDto> confirmedRequests = Collections.EMPTY_LIST;
        List<ParticipationRequestDto> rejectedRequests = Collections.EMPTY_LIST;

        if (participantLimit != 0 && availableParticipants <= 0) {
            throw new ParticipantLimitException("The participant limit = " + participantLimit +
                    " has been reached", LocalDateTime.now());
        }

        List<ParticipationRequest> requests = requestIds.stream()
                .map(this::findRequest)
                .map(this::checkRequestStatus)
                .collect(Collectors.toList());

        if (participantLimit == 0 || !event.getRequestModeration()) {
//            requests.forEach(r -> r.setStatus(RequestStatus.CONFIRMED));
            confirmedRequests = requests.stream()
                    .peek(r -> r.setStatus(RequestStatus.CONFIRMED))
                    .map(requestRepository::save)
                    .map(requestMapper::toRequestDto)
                    .collect(Collectors.toList());
            return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
        }

        if (selectedStatus.equals(RequestStatus.REJECTED)) {
//            requests.forEach(r -> r.setStatus(RequestStatus.REJECTED));
            rejectedRequests = requests.stream()
                    .peek(r -> r.setStatus(RequestStatus.REJECTED))
                    .map(requestRepository::save)
                    .map(requestMapper::toRequestDto)
                    .collect(Collectors.toList());
            return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
        }

        if (selectedStatus.equals(RequestStatus.CONFIRMED)) {
            if (potentialParticipants <= availableParticipants) {
//                requests.forEach(r -> r.setStatus(RequestStatus.CONFIRMED));
                rejectedRequests = requests.stream()
                        .peek(r -> r.setStatus(RequestStatus.CONFIRMED))
                        .map(requestRepository::save)
                        .map(requestMapper::toRequestDto)
                        .collect(Collectors.toList());
                event.setConfirmedRequests(approvedRequests + potentialParticipants);
            } else {
                confirmedRequests = requests.stream()
                        .limit(availableParticipants)
                        .peek(r -> r.setStatus(RequestStatus.CONFIRMED))
                        .map(requestRepository::save)
                        .map(requestMapper::toRequestDto)
                        .collect(Collectors.toList());
                rejectedRequests = requests.stream()
                        .skip(availableParticipants)
                        .peek(r -> r.setStatus(RequestStatus.REJECTED))
                        .map(requestRepository::save)
                        .map(requestMapper::toRequestDto)
                        .collect(Collectors.toList());
                event.setConfirmedRequests(event.getParticipantLimit());
            }
            eventRepository.save(event);
        }
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
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

    private ParticipationRequest checkRequestStatus(ParticipationRequest request) {
        if (!request.getStatus().equals(RequestStatus.PENDING)) {
            throw new NotPendingStatusException("Request must have status PENDING", LocalDateTime.now());
        }
        return request;
    }

}
