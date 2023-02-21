package ru.practicum.mainservice.controllers.publiccontrollers;

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

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class CategoryPublicController {
    private final CategoryService categoryService;

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

}
