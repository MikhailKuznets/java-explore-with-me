package ru.practicum.mainservice.controllers.admincontrollers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.dto.NewCategoryDto;
import ru.practicum.mainservice.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCompilation(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("POST-request was received at 'admin/categories' . " +
                "Create a CATEGORY: {}.", newCategoryDto);
        return new ResponseEntity<>(categoryService.createCategory(newCategoryDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> patchCategoryById(
            @PathVariable Long catId,
            @RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("PATCH-request was received at 'admin/categories/{}' . " +
                        "Patch a CATEGORY with categoryID = {}. New data = {}",
                catId, catId, newCategoryDto);
        return new ResponseEntity<>(categoryService.patchCategoryById(catId, newCategoryDto),
                HttpStatus.OK);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long catId) {
        log.info("DELETE-request was received at 'admin/categories/{}' . " +
                "Delete a CATEGORY with categoryID = {}.", catId, catId);
        categoryService.deleteCategoryById(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
