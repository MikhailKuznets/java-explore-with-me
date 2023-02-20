package ru.practicum.mainservice.controllers.privatecontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.dto.NewEventDto;
import ru.practicum.mainservice.event.dto.UpdateEventUserRequest;
import ru.practicum.mainservice.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class EventPrivateController {
    private final EventService eventService;

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> createEvent(@PathVariable @Positive Long userId,
                                                    @RequestBody @Valid NewEventDto newEventDto) {
        log.info("POST-request was received at 'users/{}/events' . " +
                "Create a EVENT: {}.", userId, newEventDto);
        return new ResponseEntity<>(eventService.createEvent(newEventDto, userId), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/events")
    public ResponseEntity<Collection<EventShortDto>> getAllUserEvents(
            @PathVariable @Positive Long userId,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        log.info("GET-request was received at 'users/{}/events?from={}&size={}' . " +
                "GET all the User's events, from User with userId = {}.", userId, from, size, userId);
        return new ResponseEntity<>(eventService.getAllUserEvents(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getPrivateEventById(@PathVariable @Positive Long userId,
                                                            @PathVariable @Positive Long eventId) {
        log.info("GET-request was received at 'users/{}/events/{}' . " +
                "Get a EVENT with eventID = {}, from USER with userID={}.", userId, eventId, eventId, userId);
        return new ResponseEntity<>(eventService.getPrivateEventById(eventId, userId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEventByUser(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("PATCH-request was received at 'users/{}/events/{}' . " +
                        "Patch a EVENT with eventID = {}, from USER with userID = {}. New Data ={}",
                userId, eventId, eventId, userId, updateEventUserRequest);
        return new ResponseEntity<>(eventService.updateEventByUser(eventId, userId, updateEventUserRequest),
                HttpStatus.OK);
    }

}
