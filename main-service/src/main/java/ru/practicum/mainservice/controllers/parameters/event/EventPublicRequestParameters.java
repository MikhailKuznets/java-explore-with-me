package ru.practicum.mainservice.controllers.publiccontrollers.parameters;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ru.practicum.mainservice.controllers.parameters.BaseEventRequestParameters;
import ru.practicum.mainservice.controllers.parameters.TimeCheckable;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class EventPublicRequestParameters extends BaseEventRequestParameters implements TimeCheckable {
    private String text;
    private Boolean paid;
    private Boolean onlyAvailable;

    @Override
    public void checkTime() {
        if (this.rangeStart == null) {
            this.rangeStart = LocalDateTime.now();
        }
        if (this.rangeEnd == null) {
            // Say Hello WarHammer 40.000 =)
            this.rangeEnd = LocalDateTime.now().plusYears(40_000);
        }
    }
}

