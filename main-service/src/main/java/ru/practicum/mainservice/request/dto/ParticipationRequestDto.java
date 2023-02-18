package ru.practicum.mainservice.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.mainservice.request.model.RequestStatus;

import java.time.LocalDateTime;

public class ParticipationRequestDto {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private RequestStatus status;
}
