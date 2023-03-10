package ru.practicum.mainservice.controllers.privatecontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.mainservice.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.mainservice.request.dto.ParticipationRequestDto;
import ru.practicum.mainservice.request.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class RequestPrivateController {
    private final RequestService requestService;

    @GetMapping("/{userId}/requests")
    public ResponseEntity<Collection<ParticipationRequestDto>> getUserRequests(
            @PathVariable @Positive Long userId) {
        log.info("GET-request was received at 'users/{}/requests' . " +
                "Get all request by USER with userId = {}.", userId, userId);
        return new ResponseEntity<>(requestService.getUserRequests(userId), HttpStatus.OK);
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<ParticipationRequestDto> createRequest(@PathVariable @Positive Long userId,
                                                                 @RequestParam @Positive Long eventId) {
        log.info("POST-request was received at 'users/{}/requests' . " +
                "A REQUEST was created from the USER with userId = {} to participate " +
                "in EVENT with eventId = {}.", userId, userId, eventId);
        return new ResponseEntity<>(requestService.createRequest(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(@PathVariable @Positive Long userId,
                                                                 @PathVariable @Positive Long requestId) {
        log.info("PATCH-request was received at 'users/{}/requests/{}/cancel' . " +
                        "The USER with userId = {} cancels the REQUEST with requestId = {} to participate in the EVENT",
                userId, requestId, userId, requestId);
        return new ResponseEntity<>(requestService.cancelRequest(userId, requestId), HttpStatus.OK);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<Collection<ParticipationRequestDto>> getRequestsForInitiator(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId) {
        log.info("GET-request was received at 'users/{}/events/{}/requests' . " +
                "Get a list of REQUESTS to participate in an EVENT with eventId = {}" +
                "created by USER with userId = {}.", userId, eventId, eventId, userId);
        return new ResponseEntity<>(requestService.getRequestsForInitiator(eventId, userId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateRequestStatus(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("PATCH-request was received at 'users/{}/events/{}/requests' . " +
                        "Get a list of REQUESTS to participate in an EVENT with eventId = {} " +
                        "created by USER with userId = {}. Data: {}",
                userId, eventId, eventId, userId, request);
        return new ResponseEntity<>(requestService.updateRequestStatus(eventId, userId, request),
                HttpStatus.OK);
    }

}
