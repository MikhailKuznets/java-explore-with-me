package ru.practicum.mainservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.service.EventService;

import javax.validation.constraints.Positive;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class PublicController {
    private final EventService eventService;
    // EVENTS
//    @GetMapping("/events")
//    public ResponseEntity<Collection<EventShortDto>> getAllUserEvents(
//            @PathVariable @Positive Long userId,
//            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
//            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
//        log.info("GET-request was received at 'users/{}/events?from={}&size={}' . " +
//                "GET all the User's events, from User with userId = {}.", userId, from, size, userId);
//        return new ResponseEntity<>(eventService.getAllUserEvents(userId, from, size), HttpStatus.OK);
//    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventFullDto> getPublicEventById(
            @PathVariable @Positive Long eventId) {
        log.info("GET-request was received at 'events/{}' . " +
                "Get public information about the event with eventId = {}.", eventId, eventId);
        return new ResponseEntity<>(eventService.getPublicEventById(eventId), HttpStatus.OK);
    }
}
