package ru.practicum.mainservice.controllers.admincontrollers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.user.dto.NewUserRequest;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/admin/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserAdminController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("POST-request was received at 'admin/users' . " +
                "Create a USER: {}.", newUserRequest);
        return new ResponseEntity<>(userService.createUser(newUserRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Collection<UserDto>> getUsers(
            @RequestParam(defaultValue = "", required = false) long[] ids,
            @RequestParam(defaultValue = "0", required = false) @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10", required = false) @Positive Integer size
    ) {
        log.info("GET-request was received at 'admin/users?ids={}&from={}&size={}' . Get USERS.",
                ids, from, size);
        return new ResponseEntity<>(userService.getUsers(ids, from, size), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable @Positive Long userId) {
        log.info("DELETE-request was received at 'admin/users/{}' . " +
                "Delete a USER with userID = {}.", userId, userId);
        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
