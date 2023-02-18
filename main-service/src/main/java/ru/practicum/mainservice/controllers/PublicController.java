package ru.practicum.mainservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.service.CategoryService;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.service.CompilationService;
import ru.practicum.mainservice.controllers.parameters.EventPublicRequestParameters;
import ru.practicum.mainservice.controllers.parameters.EventRequestSort;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.service.EventService;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class PublicController {
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;

    //  CATEGORIES
    @GetMapping("/categories")
    public ResponseEntity<Collection<CategoryDto>> getAllCategories(
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {
        log.info("GET-request was received at '/categories?from={}&size={}' . Get all categories.", from, size);
        return new ResponseEntity<>(categoryService.getAllCategories(from, size),
                HttpStatus.OK);
    }

    @GetMapping("/categories/{catId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable @Positive Long catId) {
        log.info("GET-request was received at '/categories/{}' . Get category by category ID = {}.", catId, catId);
        return new ResponseEntity<>(categoryService.getCategoryById(catId),
                HttpStatus.OK);
    }


    // EVENTS
    @GetMapping("/events")
    public ResponseEntity<Collection<EventShortDto>> getPublicAllEvents(
            @RequestParam(required = false) @NotBlank String text,
            @RequestParam(defaultValue = "[]", required = false) Long[] ids,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @FutureOrPresent LocalDateTime rangeStart,
            @RequestParam(required = false) @Future LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false", required = false) Boolean onlyAvailable,
            @RequestParam(required = false) EventRequestSort sort,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {

        EventPublicRequestParameters eventPublicRequestParameters = EventPublicRequestParameters.builder()
                .text(text)
                .catIds(List.of(ids))
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .build();

        log.info("GET-request was received at '/events' . " +
                "GET all events with search parameters  = {}.", eventPublicRequestParameters);

        return new ResponseEntity<>(eventService.getPublicEventsWithParameters(eventPublicRequestParameters,
                from, size), HttpStatus.OK);
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventFullDto> getPublicEventById(
            @PathVariable @Positive Long eventId) {
        log.info("GET-request was received at '/events/{}' . " +
                "Get public information about the event with eventId = {}.", eventId, eventId);
        return new ResponseEntity<>(eventService.getPublicEventById(eventId), HttpStatus.OK);
    }

    // COMPILATIONS

    @GetMapping("/compilations")
    public ResponseEntity<Collection<CompilationDto>> getPublicAllCompilation(
            @RequestParam(required = false) Boolean pinned) {
        log.info("GET-request was received at '/compilations' . " +
                "Get public information about all compilations with pinned = {}.", pinned);
        return new ResponseEntity<>(compilationService.getPublicAllCompilation(pinned), HttpStatus.OK);
    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<CompilationDto> getPublicCompilationById(
            @PathVariable @Positive Long compId) {
        log.info("GET-request was received at '/compilations/{}' . " +
                "Get public information about the compilation with compId = {}.", compId, compId);
        return new ResponseEntity<>(compilationService.getPublicCompilationById(compId), HttpStatus.OK);
    }
}
