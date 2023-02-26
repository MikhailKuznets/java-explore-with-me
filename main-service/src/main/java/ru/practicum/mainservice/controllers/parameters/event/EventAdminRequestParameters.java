package ru.practicum.mainservice.controllers.parameters.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ru.practicum.mainservice.event.model.EventState;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class EventAdminRequestParameters extends BaseEventRequestParameters {
    private List<Long> userIds;
    private List<EventState> states;
}
