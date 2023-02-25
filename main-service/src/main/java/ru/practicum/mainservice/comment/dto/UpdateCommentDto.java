package ru.practicum.mainservice.comment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class UpdateCommentDto {
    @Positive
    private Long commentId;

    @NotBlank
    @Size(min = 1, max = 4000)
    private String text;

}
