package ru.practicum.mainservice.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.dto.RequestCategoryDto;
import ru.practicum.mainservice.category.dto.ResponseCategoryDto;
import ru.practicum.mainservice.category.service.CategoryService;
import ru.practicum.mainservice.user.dto.RequestUserDto;
import ru.practicum.mainservice.user.dto.ResponseUserDto;
import ru.practicum.mainservice.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/admin")
@Slf4j
@RequiredArgsConstructor
@Validated
public class AdminController {
    private final UserService userService;
    private final CategoryService categoryService;

    //    Обработка admin/categories
    @PostMapping("/categories")
    public ResponseEntity<ResponseCategoryDto> createCategory(@RequestBody @Valid RequestCategoryDto requestCategoryDto) {
        log.info("POST-request was received at 'admin/categories' . " +
                "Create a CATEGORY: {}.", requestCategoryDto);
        return new ResponseEntity<>(categoryService.createCategory(requestCategoryDto), HttpStatus.CREATED);
    }

    @PatchMapping("/categories/{catId}")
    public ResponseEntity<ResponseCategoryDto> patchCategoryById(
            @PathVariable Long catId,
            @RequestBody @Valid RequestCategoryDto requestCategoryDto) {
        log.info("PATCH-request was received at 'admin/categories/{}' . " +
                        "Patch a CATEGORY with categoryID = {}. New data = {}",
                catId, catId, requestCategoryDto);
        return new ResponseEntity<>(categoryService.patchCategoryById(catId, requestCategoryDto),
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


    //    Обработка admin/users
    @PostMapping("/users")
    public ResponseEntity<ResponseUserDto> createUser(@RequestBody @Valid RequestUserDto requestUserDto) {
        log.info("POST-request was received at 'admin/users' . " +
                "Create a USER: {}.", requestUserDto);
        return new ResponseEntity<>(userService.createUser(requestUserDto), HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<Collection<ResponseUserDto>> getUsers(
            @RequestParam(defaultValue = "[]", required = false) int[] ids,
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
