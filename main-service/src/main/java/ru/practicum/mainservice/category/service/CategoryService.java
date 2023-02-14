package ru.practicum.mainservice.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.category.dto.RequestCategoryDto;
import ru.practicum.mainservice.category.dto.ResponseCategoryDto;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.exception.InvalidIdException;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public ResponseCategoryDto createCategory(RequestCategoryDto requestCategoryDto) {
        Category newCategory = categoryMapper.toCategory(requestCategoryDto);
        return categoryMapper.toCategoryDto(categoryRepository.save(newCategory));
    }

    public ResponseCategoryDto patchCategoryById(Long catId, RequestCategoryDto requestCategoryDto) {
        Category selectedCategory = findCategory(catId);

        String updatedName = requestCategoryDto.getName();
        selectedCategory.setName(updatedName);

        return categoryMapper.toCategoryDto(categoryRepository.save(selectedCategory));
    }

    public void deleteCategoryById(Long catId) {
        categoryRepository.deleteById(catId);
    }

    public Collection<ResponseCategoryDto> getAllCategories(Integer from, Integer size) {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    public ResponseCategoryDto getCategoryById(Long catId) {
        return categoryMapper.toCategoryDto(findCategory(catId));
    }

    private Category findCategory(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> {
            throw new InvalidIdException("Category with ID = " + catId + " does not exist");
        });
    }

}
