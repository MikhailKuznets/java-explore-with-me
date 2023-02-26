package ru.practicum.mainservice.controllers.parameters.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class EventPublicRequestParameters extends BaseEventRequestParameters {
    private String text;
    private Boolean paid;
    private Boolean onlyAvailable;
}