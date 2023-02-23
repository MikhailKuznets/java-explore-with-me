package ru.practicum.mainservice.controllers.admincontrollers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.controllers.admincontrollers.parameters.EventAdminRequestParameters;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.UpdateEventAdminRequest;
import ru.practicum.mainservice.event.model.EventState;
import ru.practicum.mainservice.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class EventAdminController {
    private final EventService eventService;

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEventByAdmin(
            @PathVariable @Positive Long eventId,
            @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("PATCH-request was received at 'admin/events/{}' . " +
                        "Patch a EVENT with eventID = {}, from ADMIN. New event data: {}",
                eventId, eventId, updateEventAdminRequest);
        return new ResponseEntity<>(eventService.updateEventByAdmin(eventId, updateEventAdminRequest),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<EventFullDto>> getAdminAllEvents(
            @RequestParam(defaultValue = "", required = false) List<Long> users,
            @RequestParam(defaultValue = "", required = false) List<EventState> states,
            @RequestParam(defaultValue = "", required = false) List<Long> categories,
            @RequestParam(required = false) LocalDateTime rangeStart,
            @RequestParam(required = false) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {

        EventAdminRequestParameters parameters = EventAdminRequestParameters.builder()
                .userIds(users)
                .states(states)
                .catIds(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();

        log.info("GET-request was received at 'admin/events' . " +
                "GET all EVENTS with search parameters  = {}.", parameters);

        return new ResponseEntity<>(eventService.getAdminEventsWithParameters(parameters, from, size), HttpStatus.OK);
    }

}
