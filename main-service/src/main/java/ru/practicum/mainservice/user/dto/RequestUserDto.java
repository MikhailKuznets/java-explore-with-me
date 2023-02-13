package ru.practicum.mainservice.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RequestUserDto {
    @Email(message = "Incorrect Email")
    @NotNull(message = "Email should not be null")
    private String email;

    @NotBlank(message = "Name should not be blank")
    private String name;
}
