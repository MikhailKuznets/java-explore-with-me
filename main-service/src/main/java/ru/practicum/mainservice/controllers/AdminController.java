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
import ru.practicum.mainservice.controllers.parameters.EventAdminRequestParameters;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.UpdateEventAdminRequest;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.user.dto.NewUserRequest;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
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

    //    Обработка admin/categories
    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
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
            @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("PATCH-request was received at 'admin/events/{}' . " +
                        "Patch a EVENT with eventID = {}, from ADMIN. New event data: {}",
                eventId, eventId, updateEventAdminRequest);
        return new ResponseEntity<>(eventService.updateEventByAdmin(eventId, updateEventAdminRequest),
                HttpStatus.OK);
    }

    @GetMapping("/events")
    public ResponseEntity<Collection<EventFullDto>> getAdminAllEvents(
            @RequestParam(defaultValue = "[]",required = false) Long[] userIds,
            @RequestParam(required = false) String states,
            @RequestParam(required = false) String catIds,
            @RequestParam(required = false) LocalDateTime rangeStart,
            @RequestParam(required = false) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size) {

        EventAdminRequestParameters parameters = EventAdminRequestParameters.builder()
                .userIds(Collections.EMPTY_LIST)
                .states(Collections.EMPTY_LIST)
                .catIds(Collections.EMPTY_LIST)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();

        log.error(userIds.toString());
        log.error(states);
        log.error(catIds);

        log.info("GET-request was received at '/events' . " +
                "GET all events with search parameters  = {}.", parameters);


        return new ResponseEntity<>(null, HttpStatus.OK);
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
            @RequestParam(defaultValue = "[]", required = false) long[] ids,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size
    ) {
        log.info("GET-request was received at 'admin/users?ids={}&from={}&size={}' . Get users.",
                ids, from, size);
        return new ResponseEntity<>(userService.getUsers(ids, from, size), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
        log.info("DELETE-request was received at 'admin/users/{}' . " +
                "Delete a USER with UserID = {}.", userId, userId);
        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


//    Обработка admin/compilations


}
