package ru.practicum.mainservice.category.service;

import org.springframework.stereotype.Service;
import ru.practicum.mainservice.category.dto.RequestCategoryDto;
import ru.practicum.mainservice.category.dto.ResponseCategoryDto;

import java.util.Collection;

@Service
public class CategoryService {
    public ResponseCategoryDto createCategory(RequestCategoryDto requestCategoryDto) {
        return null;
    }

    public ResponseCategoryDto patchCategoryById(Long catId, RequestCategoryDto requestCategoryDto) {
        return null;
    }

    public void deleteCategoryById(Long catId) {
    }

    public Collection<ResponseCategoryDto> getAllCategories(Integer from, Integer size) {
        return null;
    }

    public ResponseCategoryDto getCategoryById(Long catId) {
        return null;
    }
}
