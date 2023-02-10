package ru.practicum.mainservice.user.dto;

import lombok.Data;

@Data
public class ResponseUserDto {

    private final Long id;

    private final String email;

    private final String name;

}
