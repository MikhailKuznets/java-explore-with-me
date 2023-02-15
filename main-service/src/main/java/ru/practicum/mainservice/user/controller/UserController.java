package ru.practicum.mainservice.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.NewEventDto;
import ru.practicum.mainservice.event.dto.UpdateEventUserRequest;
import ru.practicum.mainservice.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserController {
    private final EventService eventService;

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> createEvent(@PathVariable @Positive Long userId,
                                                    @RequestBody @Valid NewEventDto newEventDto) {
        log.info("POST-request was received at 'users/{}/events' . " +
                "Create a EVENT: {}.", userId, newEventDto);
        return new ResponseEntity<>(eventService.createEvent(newEventDto, userId), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getEventById(@PathVariable @Positive Long userId,
                                                     @PathVariable @Positive Long eventId) {
        log.info("GET-request was received at 'users/{}/events/{}' . " +
                "Get a EVENT with eventID = {}, from USER with userID={}.", userId, eventId, eventId, userId);
        return new ResponseEntity<>(eventService.getEventById(eventId, userId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEventByUser(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("PATCH-request was received at 'users/{}/events/{}' . " +
                "Patch a EVENT with eventID = {}, from USER with userID={}.", userId, eventId, eventId, userId);
        return new ResponseEntity<>(eventService.updateEventByUser(eventId, userId, updateEventUserRequest),
                HttpStatus.OK);
    }

}
