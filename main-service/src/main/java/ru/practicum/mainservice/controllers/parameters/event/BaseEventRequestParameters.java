package ru.practicum.mainservice.controllers.parameters.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.mainservice.controllers.parameters.TimeCheckable;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseEventRequestParameters implements TimeCheckable {
    protected List<Long> catIds;
    protected LocalDateTime rangeStart;
    protected LocalDateTime rangeEnd;

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
