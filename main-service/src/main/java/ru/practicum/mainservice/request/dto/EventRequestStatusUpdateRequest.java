package ru.practicum.mainservice.request.dto;

import lombok.Data;
import ru.practicum.mainservice.request.model.RequestStatus;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    private final List<Long> requestIds;
    private final RequestStatus status;
}
