package ru.practicum.mainservice.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.category.dto.CategoryDto;
import ru.practicum.mainservice.category.dto.NewCategoryDto;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.exception.InvalidIdException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        Category newCategory = categoryMapper.toCategory(newCategoryDto);
        return categoryMapper.toCategoryDto(categoryRepository.save(newCategory));
    }

    public CategoryDto patchCategoryById(Long catId, NewCategoryDto newCategoryDto) {
        Category selectedCategory = findCategory(catId);

        String updatedName = newCategoryDto.getName();
        selectedCategory.setName(updatedName);

        return categoryMapper.toCategoryDto(categoryRepository.save(selectedCategory));
    }

    public void deleteCategoryById(Long catId) {
        categoryRepository.deleteById(catId);
    }

    public Collection<CategoryDto> getAllCategories(Integer from, Integer size) {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long catId) {
        return categoryMapper.toCategoryDto(findCategory(catId));
    }

    private Category findCategory(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> {
            throw new InvalidIdException("Category", catId, LocalDateTime.now());
        });
    }

}
