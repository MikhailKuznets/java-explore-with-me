package ru.practicum.mainservice.comment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdateCommentDto {
    @NotBlank
    @Size(min = 1, max = 4000)
    private String text;

}
