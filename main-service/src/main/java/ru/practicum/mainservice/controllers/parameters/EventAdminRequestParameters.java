package ru.practicum.mainservice.controllers.parameters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventAdminRequestParameters {
    private List<Long> userIds;
    private EventState state;
    private List<Long> catIds;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
}
