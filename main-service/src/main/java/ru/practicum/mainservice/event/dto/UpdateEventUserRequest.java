package ru.practicum.mainservice.event.dto;

import lombok.Data;
import ru.practicum.mainservice.event.model.Location;
import ru.practicum.mainservice.event.model.UpdateEventUserState;

import java.time.LocalDateTime;

@Data
public class UpdateEventUserRequest {
    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;

    private UpdateEventUserState stateAction;
    private String title;
}
