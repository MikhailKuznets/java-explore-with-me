package ru.practicum.mainservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.dto.NewCategoryDto;
import ru.practicum.mainservice.category.service.CategoryService;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.dto.UpdateCompilationRequest;
import ru.practicum.mainservice.compilation.service.CompilationService;
import ru.practicum.mainservice.controllers.parameters.EventAdminRequestParameters;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.UpdateEventAdminRequest;
import ru.practicum.mainservice.event.model.EventState;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.user.dto.NewUserRequest;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@Slf4j
@RequiredArgsConstructor
@Validated
public class AdminController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;

    //    Обработка admin/categories
    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCompilation(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("POST-request was received at 'admin/categories' . " +
                "Create a CATEGORY: {}.", newCategoryDto);
        return new ResponseEntity<>(categoryService.createCategory(newCategoryDto), HttpStatus.CREATED);
    }

    @PatchMapping("/categories/{catId}")
    public ResponseEntity<CategoryDto> patchCategoryById(
            @PathVariable Long catId,
            @RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("PATCH-request was received at 'admin/categories/{}' . " +
                        "Patch a CATEGORY with categoryID = {}. New data = {}",
                catId, catId, newCategoryDto);
        return new ResponseEntity<>(categoryService.patchCategoryById(catId, newCategoryDto),
                HttpStatus.OK);
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long catId) {
        log.info("DELETE-request was received at 'admin/categories/{}' . " +
                "Delete a CATEGORY with categoryID = {}.", catId, catId);
        categoryService.deleteCategoryById(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    //    Обработка admin/events
    @PatchMapping("/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEventByAdmin(
            @PathVariable @Positive Long eventId,
            @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("PATCH-request was received at 'admin/events/{}' . " +
                        "Patch a EVENT with eventID = {}, from ADMIN. New event data: {}",
                eventId, eventId, updateEventAdminRequest);
        return new ResponseEntity<>(eventService.updateEventByAdmin(eventId, updateEventAdminRequest),
                HttpStatus.OK);
    }

    @GetMapping("/events")
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
                "GET all events with search parameters  = {}.", parameters);

        return new ResponseEntity<>(eventService.getAdminEventsWithParameters(parameters, from, size), HttpStatus.OK);
    }


    //    Обработка admin/users
    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("POST-request was received at 'admin/users' . " +
                "Create a USER: {}.", newUserRequest);
        return new ResponseEntity<>(userService.createUser(newUserRequest), HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<Collection<UserDto>> getUsers(
            @RequestParam(defaultValue = "", required = false) long[] ids,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size
    ) {
        log.info("GET-request was received at 'admin/users?ids={}&from={}&size={}' . Get users.",
                ids, from, size);
        return new ResponseEntity<>(userService.getUsers(ids, from, size), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable @Positive Long userId) {
        log.info("DELETE-request was received at 'admin/users/{}' . " +
                "Delete a USER with UserID = {}.", userId, userId);
        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    //    Обработка admin/compilations
    @PostMapping("/compilations")
    public ResponseEntity<CompilationDto> createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("POST-request was received at 'admin/compilations' . " +
                "Create a COMPILATION: {}.", newCompilationDto);
        return new ResponseEntity<>(compilationService.createCompilation(newCompilationDto), HttpStatus.CREATED);
    }

    @PatchMapping("/compilations/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(
            @PathVariable @Positive Long compId,
            @RequestBody UpdateCompilationRequest updateCompilationDto) {
        log.info("PATCH-request was received at 'admin/compilations/{}' . " +
                "Update the COMPILATION with compId = {}. New DATA: {}.", compId, compId, updateCompilationDto);
        return new ResponseEntity<>(compilationService.updateCompilation(compId, updateCompilationDto),
                HttpStatus.OK);
    }

    @DeleteMapping("/compilations/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable @Positive Long compId) {
        log.info("DELETE-request was received at 'admin/compilations/{}' . " +
                "Delete a COMPILATION with compId = {}.", compId, compId);
        compilationService.deleteCompilationById(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
