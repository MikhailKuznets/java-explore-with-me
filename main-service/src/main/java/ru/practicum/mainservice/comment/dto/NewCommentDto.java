package ru.practicum.mainservice.comment.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class NewCommentDto {
    @NotBlank
    @Size(min = 1, max = 4000)
    private String text;

}
