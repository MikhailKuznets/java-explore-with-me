package ru.practicum.mainservice.category.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RequestCategoryDto {
    @NotBlank(message = "Category name should not be blank")
    @Size(min = 2, max = 255)
    private final String name;
}
